package search.analyzers;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.interfaces.IList;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are not
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        // make ChainhashDictionary for storing graph for return
        IDictionary<URI, ISet<URI>> returnGraph = new ChainedHashDictionary<URI, ISet<URI>>();
        // create the set storing all URI in the inputed web pages
        ISet<URI> uriPages = new ChainedHashSet<URI>();
        // get all page URI and store to the set
        for (Webpage page : webpages) {
            uriPages.add(page.getUri());
        }
        for (Webpage page : webpages) {
            URI nameOfPage = page.getUri();
            ISet<URI> tempSet = new ChainedHashSet<URI>();
            IList<URI> links = page.getLinks();
            // check whether list to links has other page's URI or not
            for (URI nameOfLink : links) {
                if (uriPages.contains(nameOfLink)) {
                    if (nameOfLink != nameOfPage) {
                        tempSet.add(nameOfLink);
                    }
                }
            }

            returnGraph.put(nameOfPage, tempSet);
        }
        return returnGraph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> initialPageRank = new ChainedHashDictionary<URI, Double>();
        double initialRank = 1.0 / graph.size();
        // create ChainedHashDictionary that has initial rank
        for (KVPair<URI, ISet<URI>> page : graph) {
            initialPageRank.put(page.getKey(), initialRank);
        }
        IDictionary<URI, Double> newPageRank = new ChainedHashDictionary<URI, Double>();
        // create ChainedHashDictionary that has 0.0 as a rank score
        for (KVPair<URI, ISet<URI>> page : graph) {
            newPageRank.put(page.getKey(), 0.0);
        }

        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            IDictionary<URI, Double> result = new ChainedHashDictionary<URI, Double>();
            for (KVPair<URI, ISet<URI>> node : graph) {
                URI uri = node.getKey();
                double uniqueLink = node.getValue().size();
                if (uniqueLink != 0) {
                    // calculate rank based on how many links they have
                    for (URI value : node.getValue()) {
                        newPageRank.put(value, newPageRank.get(value)
                                + (decay * initialPageRank.get(uri)) / uniqueLink);
                    }
                } else {
                    // add old rank to current rank of all pages
                    for (KVPair<URI, Double> value : newPageRank) {
                        newPageRank.put(value.getKey(), newPageRank.get(value.getKey())
                                +  (decay * initialPageRank.get(uri)) / (double) graph.size());
                    }
                }
            }

            // add  1 - d / N to every score of all pages
            for (KVPair<URI, Double> value : newPageRank) {
                result.put(value.getKey(), value.getValue() + (1 - decay)/ (double) graph.size());
                newPageRank.put(value.getKey(), 0.0);
            }


            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean converge = true;
            for (KVPair<URI, Double> element : initialPageRank) {
                URI key = element.getKey();
                if (Math.abs((initialPageRank.get(key) - result.get(key))) > epsilon) {
                    converge = false;
                }
            }
            // return result immediately
            if (converge) {
                return result;
            }
            // change old rank to new rank
            initialPageRank = result;
        }
        return initialPageRank;
    }


    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
