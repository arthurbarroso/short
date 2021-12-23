(ns short.products.handlers-test
  (:require [short.products.handlers :as h]
            [test-helpers :as th]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.products.schemas :as s]
            [short.products.contracts :as c]
            [short.shared :as shared]))

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
    :title "some-title"
    :price 30}
    ;; :quantity 2}
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

(deftest products-get-product-by-slug-handler-test
  (testing "Returns an existing product using it's slug to search the database"
    (let [database database-conn
          crp (h/create-product! (p {:slug "maneiro"
                                     :sku "coolio"}) @database)
          result (h/get-product-by-slug! (:product/slug crp) @database)]
      (is (true? (ml/validate c/ProductOut result)))
      (is (= (:product/slug result) "maneiro"))))
  (testing "Returns nil for a non-existing product slug"
    (let [database database-conn
          result (h/get-product-by-slug! "failure39140180493109" @database)]
      (is (true? (ml/validate c/ProductOut result)))
      (is (nil? result)))))

(deftest products-get-product-by-uuid-handler-test
  (testing "Returns an existing product using it's uuid to search the database"
    (let [database database-conn
          crp (h/create-product! (p {:slug "maneiro123"
                                     :sku "coolio123"}) @database)
          result (h/get-product-by-uuid! (:product/uuid crp) @database)]
      (is (true? (ml/validate c/ProductOut result)))
      (is (= (:product/slug result) "maneiro123"))))
  (testing "Returns nil for a non-existing product uuid"
    (let [database database-conn
          result (h/get-product-by-uuid! (shared/generate-uuid!) @database)]
      (is (true? (ml/validate c/ProductOut result)))
      (is (nil? result)))))
