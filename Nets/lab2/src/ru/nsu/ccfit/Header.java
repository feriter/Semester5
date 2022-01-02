package ru.nsu.ccfit;

class Header {
    private String fileName;
    private Long size;

    public Header() {

    }

    public Header(String name, Long s) {
        fileName = name;
        size = s;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getSize() {
        return size;
    }
}
