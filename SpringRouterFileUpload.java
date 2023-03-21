
@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route(RequestPredicates.POST("/upload")
                .and(RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA)), this::uploadHandler);
    }

    private Mono<ServerResponse> uploadHandler(ServerRequest request) {
        return request.body(BodyExtractors.toMultipartData())
                .flatMap(parts -> {
                    FilePart file = (FilePart) parts.toSingleValueMap().get("file");
                    return file.transferTo(new File("uploadedFile.txt"))
                            .then(ServerResponse.ok().build());
                });
    }
}
