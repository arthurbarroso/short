(ns short.middlewares-test
  (:require [short.middlewares :as mi]
            [clojure.test :refer [deftest testing is]]))

#_(defn exception-handler [message exception request]
    {:status 500
     :body {:message message
            :excepetion (.getClass exception)
            :data (ex-data exception)
            :uri (:uri request)}})

(deftest middlewares-exception-handler-test
  (testing "Returns an exception map"
    (let [res-map (mi/exception-handler
                   "Something wrong happened"
                   (Exception. "Some expcetion")
                   {:uri "test"})]
      (is (= (:status res-map) 500))
      (is (= "Something wrong happened"
             (-> res-map :body :message))))))
