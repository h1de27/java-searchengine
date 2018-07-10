package search.analyzers;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;


    // This filed store Norm score of TF-IDF vector of all documents for using compute relevance method.
    private IDictionary<URI, Double> documentNorm;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        this.documentNorm = new ChainedHashDictionary<URI, Double>();
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> numOfWords = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Double> idfScore = new ChainedHashDictionary<String, Double>();
        int numOfPages = pages.size();
        for (Webpage page : pages) {
            IList<String> words = page.getWords();
            ISet<String> temp = new ChainedHashSet<String>();
            // Store all words in set
            for (String word : words) {
                temp.add(word);
            }
            // add all words and how many time show up to ChainedHashDictionary
            for (String word : temp) {
                if (!numOfWords.containsKey(word)) {
                    numOfWords.put(word, 1.0);
                } else {
                    double count = numOfWords.get(word);
                    numOfWords.put(word, count + 1.0);
                }
            }
        }
        // calculate score
        for (KVPair<String, Double> pair : numOfWords) {
            String word = pair.getKey();
            Double count = pair.getValue();
            Double score = Math.log(((double) numOfPages) / count);
            idfScore.put(word, score);
        }
        return idfScore;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<String, Double>();
        int numOfWords = words.size();
        for (String word : words) {
            if (!tfScores.containsKey(word)) {
                tfScores.put(word, 1.0 / numOfWords);
            } else {
                double currScore = tfScores.get(word);
                tfScores.put(word, (1.0 / numOfWords + currScore));
            }
        }
        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {

        IDictionary<URI, IDictionary<String, Double>> vectors = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IDictionary<String, Double> relevance = new ChainedHashDictionary<>();
            IList<String> words = page.getWords();
            // calculate tf Score
            IDictionary<String, Double> tfScores = computeTfScores(words);
            // this is for storing document norm score
            double output = 0.0;
            for (String word : words) {
                if (!relevance.containsKey(word)) {
                    Double relScore = tfScores.get(word) * idfScores.get(word);
                    relevance.put(word, relScore);
                    output += relScore * relScore;
                }
            }
            // at the same time when calculate relevance, calculate document norm score
            documentNorm.put(page.getUri(), output);
            vectors.put(page.getUri(), relevance);
        }
        return vectors;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        
        IDictionary<String, Double> docVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryTfScore = computeTfScores(query);
        IDictionary<String, Double> queryVector= new ChainedHashDictionary<String, Double>();

        double queryNorm = 0.0;
        // calculate query's score
        for (String word : query) {
            if (queryTfScore.containsKey(word)) {
                double idfScore;
                if (idfScores.containsKey(word)) {
                    idfScore = idfScores.get(word);
                } else {
                    idfScore = 0.0;
                }
                double relScore = queryTfScore.get(word) * idfScore;
                queryVector.put(word, relScore);
                queryNorm += relScore * relScore;
            }
        }

        // calculate numerator
        double numerator = 0.0;
        for (String word : query) {
            double docWordScore = 0.0;
            if (docVector.containsKey(word)) {
                docWordScore = docVector.get(word);
            }
            double queryWordScore = queryVector.get(word);
            numerator += docWordScore * queryWordScore;
        }
        double denominator = Math.sqrt(documentNorm.get(pageUri)) * Math.sqrt(queryNorm);

        if (denominator != 0) {
            return numerator / denominator;
        } else {
            return 0.0;
        }
    }
}
