package com.unir.books.data;

import java.util.*;

import com.unir.books.model.db.Book;
import com.unir.books.model.response.AggregationDetails;
import com.unir.books.model.response.ProductsQueryResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    @Value("${server.fullAddress}")
    private String serverFullAddress;

    // Esta clase (y bean) es la unica que usan directamente los servicios para
    // acceder a los datos.
    private final BookRepositoryEs bookRepository;
    private final ElasticsearchOperations elasticClient;

    private final String[] descriptionSearchFields = {"description", "description._2gram", "description._3gram"};

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Boolean delete(Book book) {
        bookRepository.delete(book);
        return Boolean.TRUE;
    }

	public Optional<Book> findById(String id) {
		return bookRepository.findById(id);
	}

    @SneakyThrows
    public ProductsQueryResponse findBooks(String name, String description,  Long isbn, String genre, String language, String author, Boolean aggregate) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(genre)) {
            querySpec.must(QueryBuilders.termQuery("genre", genre));
        }

        if (!StringUtils.isEmpty(language)) {
            querySpec.must(QueryBuilders.termQuery("language", language));
        }

        if (isbn != null) {
            querySpec.must(QueryBuilders.matchQuery("isbn", isbn));
        }

        if (!StringUtils.isEmpty(name)) {
            querySpec.must(QueryBuilders.matchQuery("name", name));
        }

        if (!StringUtils.isEmpty(author)) {
            querySpec.must(QueryBuilders.matchQuery("author", author));
        }

        if (!StringUtils.isEmpty(description)) {
            querySpec.must(QueryBuilders.multiMatchQuery(description, descriptionSearchFields).type(Type.BOOL_PREFIX));
        }

        //Si no he recibido ningun parametro, busco todos los elementos.
        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Filtro implicito
        //No le pido al usuario que lo introduzca pero lo aplicamos proactivamente en todas las peticiones
        //En este caso, que los bookos sean visibles (estado correcto de la entidad)
        querySpec.must(QueryBuilders.termQuery("visible", true));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        if (aggregate) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("Language Aggregation").field("language").size(1000));
            nativeSearchQueryBuilder.withMaxResults(0);
        }

        //Opcionalmente, podemos paginar los resultados
        // nativeSearchQueryBuilder.withPageable(PageRequest.of(0, 10));

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Book> result = elasticClient.search(query, Book.class);

        List<AggregationDetails> responseAggs = new LinkedList<>();

        if (result.hasAggregations()) {
            Map<String, Aggregation> aggs = result.getAggregations().asMap();
            ParsedStringTerms countryAgg = (ParsedStringTerms) aggs.get("Language Aggregation");

            //Componemos una URI basada en serverFullAddress y query params para cada argumento, siempre que no viniesen vacios
            String queryParams = getQueryParams(name, description, genre, language, isbn, author);
            countryAgg.getBuckets()
                    .forEach(
                            bucket -> responseAggs.add(
                                    new AggregationDetails(
                                            bucket.getKey().toString(),
                                            (int) bucket.getDocCount(),
                                            serverFullAddress + "/books?language=" + bucket.getKey() + queryParams)));
        }
        return new ProductsQueryResponse(result.getSearchHits().stream().map(SearchHit::getContent).toList(), responseAggs);
    }

    /**
     * Componemos una URI basada en serverFullAddress y query params para cada argumento, siempre que no viniesen vacios
     *
     * @param name        - nombre del booko
     * @param description - descripcion del booko
     * @param genre     - pais del booko
     * @param language     - pais del booko
     * @param isbn     - pais del booko
     * @param author     - pais del booko
     *
     * @return
     */
    private String getQueryParams(String name, String description, String genre, String language, Long isbn, String author) {
        String queryParams = (StringUtils.isEmpty(name) ? "" : "&name=" + name)
                + (StringUtils.isEmpty(description) ? "" : "&description=" + description)
                + (StringUtils.isEmpty(genre) ? "" : "&genre=" + genre)
                + (StringUtils.isEmpty(language) ? "" : "&language=" + language)
                + (isbn == null ? "" : "&isbn" + isbn)
                + (StringUtils.isEmpty(author) ? "" : "&author=" + author);
        // Eliminamos el ultimo & si existe
        return queryParams.endsWith("&") ? queryParams.substring(0, queryParams.length() - 1) : queryParams;
    }
}
