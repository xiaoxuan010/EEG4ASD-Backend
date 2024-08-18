package space.astralbridge.eeg4asd.model;

import lombok.Data;

@Data
public class Prediction {
    public int video_index;
    public int left_index;
    public int right_index;
    public int label;

}