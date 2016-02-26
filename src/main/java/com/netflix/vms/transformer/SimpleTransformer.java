package com.netflix.vms.transformer;

import com.netflix.hollow.read.engine.HollowReadStateEngine;
import com.netflix.hollow.util.SimultaneousExecutor;
import com.netflix.hollow.write.HollowWriteStateEngine;
import com.netflix.hollow.write.objectmapper.HollowObjectMapper;
import com.netflix.vms.transformer.hollowinput.VMSHollowVideoInputAPI;
import com.netflix.vms.transformer.hollowinput.VideoDisplaySetHollow;
import com.netflix.vms.transformer.hollowoutput.CompleteVideo;
import com.netflix.vms.transformer.hollowoutput.CompleteVideoFacetData;
import com.netflix.vms.transformer.hollowoutput.ISOCountry;
import com.netflix.vms.transformer.hollowoutput.Video;
import com.netflix.vms.transformer.hollowoutput.VideoCollectionsData;
import com.netflix.vms.transformer.hollowoutput.VideoMetaData;
import com.netflix.vms.transformer.index.VMSTransformerIndexer;
import com.netflix.vms.transformer.modules.collections.VideoCollectionsDataHierarchy;
import com.netflix.vms.transformer.modules.collections.VideoCollectionsModule;
import com.netflix.vms.transformer.modules.drmsystem.DrmSystemModule;
import com.netflix.vms.transformer.modules.meta.VideoMetaDataModule;
import com.netflix.vms.transformer.modules.originserver.OriginServersModule;

import java.util.HashMap;
import java.util.Map;

public class SimpleTransformer {

    public HollowWriteStateEngine transform(HollowReadStateEngine inputStateEngine, VMSHollowVideoInputAPI api) throws Exception {

        VMSTransformerIndexer indexer = new VMSTransformerIndexer(inputStateEngine, new SimultaneousExecutor());

        final ShowHierarchyInitializer hierarchyInitializer = new ShowHierarchyInitializer(api, indexer);
        final VideoCollectionsModule collectionsBuilder = new VideoCollectionsModule(api, indexer);
        final VideoMetaDataModule metadataModule = new VideoMetaDataModule(api, indexer);

        HollowWriteStateEngine writeStateEngine = new HollowWriteStateEngine();  //TODO: Need to define a HashCodeFinder.
        final HollowObjectMapper objectMapper = new HollowObjectMapper(writeStateEngine);

        SimultaneousExecutor executor = new SimultaneousExecutor();

        long startTime = System.currentTimeMillis();

        for(final VideoDisplaySetHollow displaySet : api.getAllVideoDisplaySetHollow()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Map<String, ShowHierarchy> showHierarchiesByCountry = hierarchyInitializer.getShowHierarchiesByCountry(displaySet);

                    if(showHierarchiesByCountry != null) {
                        Map<String, VideoCollectionsDataHierarchy> vcdByCountry = collectionsBuilder.buildVideoCollectionsDataByCountry(showHierarchiesByCountry);
                        Map<String, Map<Integer, VideoMetaData>> vmdByCountry = metadataModule.buildVideoMetaDataByCountry(showHierarchiesByCountry);

                        if(vcdByCountry != null)
                            writeJustTheCurrentData(vcdByCountry, vmdByCountry, objectMapper);
                    }
                }
            });
        }

        new DrmSystemModule(api, objectMapper).transform();
        new OriginServersModule(api, objectMapper, indexer).transform();

        executor.awaitSuccessfulCompletion();

        long endTime = System.currentTimeMillis();
        System.out.println("Processed all videos in " + (endTime - startTime) + "ms");

        return writeStateEngine;
    }

    private void writeJustTheCurrentData(Map<String, VideoCollectionsDataHierarchy> vcdByCountry,
            Map<String, Map<Integer, VideoMetaData>> vmdByCountry,
            HollowObjectMapper objectMapper) {

        for(Map.Entry<String, VideoCollectionsDataHierarchy> countryHierarchyEntry : vcdByCountry.entrySet()) {
            String countryId = countryHierarchyEntry.getKey();
            ISOCountry country = getCountry(countryId);
            VideoCollectionsDataHierarchy hierarchy = countryHierarchyEntry.getValue();

            CompleteVideo topNode = new CompleteVideo();
            topNode.country = country;
            topNode.facetData = new CompleteVideoFacetData();
            topNode.facetData.videoCollectionsData = hierarchy.getTopNode();
            topNode.id = topNode.facetData.videoCollectionsData.topNode;
            topNode.facetData.videoMetaData = vmdByCountry.get(countryId).get(topNode.id.value);

            objectMapper.addObject(topNode);

            if(topNode.facetData.videoCollectionsData.nodeType == VideoCollectionsDataHierarchy.SHOW) {
                int sequenceNumber = 0;

                for(Map.Entry<Integer, VideoCollectionsData> showEntry : hierarchy.getOrderedSeasons().entrySet()) {
                    CompleteVideo showNode = new CompleteVideo();
                    showNode.country = country;
                    showNode.id = new Video(showEntry.getKey().intValue());
                    showNode.facetData = new CompleteVideoFacetData();
                    showNode.facetData.videoCollectionsData = showEntry.getValue();
                    showNode.facetData.videoMetaData = vmdByCountry.get(countryId).get(showNode.id.value);

                    objectMapper.addObject(showNode);

                    for(Map.Entry<Integer, VideoCollectionsData> episodeEntry : hierarchy.getOrderedSeasonEpisodes(++sequenceNumber).entrySet()) {
                        CompleteVideo episodeNode = new CompleteVideo();
                        episodeNode.country = country;
                        episodeNode.id = new Video(episodeEntry.getKey().intValue());
                        episodeNode.facetData = new CompleteVideoFacetData();
                        episodeNode.facetData.videoCollectionsData = episodeEntry.getValue();
                        episodeNode.facetData.videoMetaData = vmdByCountry.get(countryId).get(episodeNode.id.value);

                        objectMapper.addObject(episodeNode);
                    }
                }
            }

            for(Map.Entry<Integer, VideoCollectionsData> supplementalEntry : hierarchy.getSupplementalVideosCollectionsData().entrySet()) {
                CompleteVideo supplementalNode = new CompleteVideo();
                supplementalNode.country = country;
                supplementalNode.id = new Video(supplementalEntry.getKey().intValue());
                supplementalNode.facetData = new CompleteVideoFacetData();
                supplementalNode.facetData.videoCollectionsData = supplementalEntry.getValue();
                supplementalNode.facetData.videoMetaData = vmdByCountry.get(countryId).get(supplementalNode.id.value);

                objectMapper.addObject(supplementalNode);
            }
        }
    }

    private Map<String, ISOCountry> countries = new HashMap<String, ISOCountry>();
    private ISOCountry getCountry(String id) {
        ISOCountry country = countries.get(id);
        if(country == null) {
            country = new ISOCountry(id);
            countries.put(id, country);
        }
        return country;
    }

}
