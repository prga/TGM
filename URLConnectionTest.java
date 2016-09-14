  enum TransferKind {
    CHUNKED() {
      @Override void setBody(MockResponse response, Buffer content, int chunkSize)
          throws IOException {
        response.setChunkedBody(content, chunkSize);
        //comment2
      }
      @Override void setForRequest(HttpURLConnection connection, int contentLength) {
        connection.setChunkedStreamingMode(5);
      }
    },
    FIXED_LENGTH() {
      @Override void setBody(MockResponse response, Buffer content, int chunkSize) {
        response.setBody(content);
      }
      @Override void setForRequest(HttpURLConnection connection, int contentLength) {
        connection.setFixedLengthStreamingMode(contentLength);
      }
    },
    END_OF_STREAM() {
      @Override void setBody(MockResponse response, Buffer content, int chunkSize) {
        response.setBody(content);
        response.setSocketPolicy(DISCONNECT_AT_END);
        for (Iterator<String> h = response.getHeaders().iterator(); h.hasNext(); ) {
          if (h.next().startsWith("Content-Length:")) {
            h.remove();
            break;
          }
        }
      }
      @Override void setForRequest(HttpURLConnection connection, int contentLength) {
      }
    };
  }