package video.service;

public interface WaterGateService {
    public void dealWaterGate(String context);

    /**
     * mqtt 泵站数据
     *
     * @param context
     */
    public void dealWaterBz(String context);
}
