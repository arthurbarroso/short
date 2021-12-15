(ns short.products.handlers-test
  (:require [short.products.handlers :as h]
            [test-helpers :as th]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.products.schemas :as s]
            [short.products.contracts :as c]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "products-tests-handlers"))
    (f)))

(defn p [opts]
  (merge
   {:sku "fasjfsaoij123"
    :active true
    :slug "some-slug"
    :price 30
    :quantity 2}
   opts))

(deftest products-create-product-handler-test
  (testing "Creates a new product from external data and prepares
            it to be sent in the output"
    (let [database database-conn
          result (h/create-product! (p {}) @database)]
      (is (true? (ml/validate c/ProductOut result))))))

(deftest products-check-product-existence-by-slug-handler-test
  (testing "Checks the existence of a product with a slug coming from external data.
            Should return data that conforms to s/ProductQueryResult"
    (let [database database-conn
          _ (h/create-product! (p {:slug "umdoistres"
                                   :sku "skuuuuuul"})
                               @database)
          result (h/check-product-existence-by-slug! {:slug "umdoistres"}
                                                     @database)]
      (is (true? (ml/validate s/ProductQueryResult result)))))
  (testing "Returns an empty s/ProductQuerResult when there is no product with given slug"
    (let [database database-conn
          _ (h/create-product! (p {:slug "umdoistres2"
                                   :sku "skuuuuuull2"})
                               @database)
          result (h/check-product-existence-by-slug! {:slug "umdoistres341"}
                                                     @database)]
      (is (empty? result))
      (is (true? (ml/validate s/ProductQueryResult result))))))

#_(defn check-product-existence-by-slug!
    {:malli/schema [:=> [:cat c/ProductData :any] s/ExistingProduct]}
    [product-input db]
    (db/get-product-by-slug! (:slug product-input) db))
