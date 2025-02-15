package osSim;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Job {
    private int jobID;
    private List<String> code;
    private List<String> data;

    // Default constructor required for Jackson
    public Job() {}

    public Job(@JsonProperty("job_id") int jobID,
               @JsonProperty("code") List<String> code,
               @JsonProperty("data") List<String> data) {
        this.jobID = jobID;
        this.code = code;
        this.data = data;
    }

    // Getters and setters
    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
