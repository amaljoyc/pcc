package com.amaljoyc.pcc.api.dto;

/**
 * Created by achemparathy on 04.12.17.
 */
public class UploadDto {

    private Integer count;
    private Long timestamp;

    public UploadDto() {
    }

    public UploadDto(Integer count, Long timestamp) {
        this.count = count;
        this.timestamp = timestamp;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadDto dto = (UploadDto) o;

        if (count != null ? !count.equals(dto.count) : dto.count != null) return false;
        return timestamp != null ? timestamp.equals(dto.timestamp) : dto.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = count != null ? count.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UploadDto{" +
                "count=" + count +
                ", timestamp=" + timestamp +
                '}';
    }
}
