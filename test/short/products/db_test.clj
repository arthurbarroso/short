(ns short.products.db-test
  (:require [short.products.db :as db]
            [short.products.schemas :as s]
            [malli.core :as m]
            [test-helpers :as th]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.shared :as shared]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "products-tests"))
    (f)))

(defn create-product [opts]
  (merge
   {:product/sku "fasjfsaoij123"
    :product/active true
    :product/slug "some-slug"
    :product/title "some-title"
    :product/price (bigdec 30)
    :product/quantity 2
    :product/uuid (shared/generate-uuid!)
    :product/created_at (shared/get-current-inst!)}
   opts))

(deftest produts-create-product-test
  (testing "Creates a new product"
    (let [database database-conn
          result (db/create-product! (create-product {}) @database)]
      (is (not (nil? (:tx-data result))))
      (is (true? (m/validate s/ProductTx result))))))

(deftest procuts-get-product-test
  (testing "Finds an user"
    (let [database database-conn
          usertx (db/create-product! (create-product
                                      {:product/sku "test-sku"
                                       :product/slug "slug-test"})
                                     @database)
          eid (shared/tempid->eid usertx)
          result (db/get-product! eid @database)]
      (is (true? (m/validate s/ExistingProduct result))))))

(deftest products-get-product-test
  (testing "Finds a product by it's uuid"
    (let [u (create-product {:product/sku "find-me"
                             :product/slug "slug-test-find"})
          database database-conn
          _ (db/create-product! u
                                @database)
          result (db/get-product-by-uuid! (:product/uuid u) @database)]
      (is (true? (m/validate s/ProductQueryResult result)))))
  (testing "Returns nil for a non-existent uuid"
    (let [database database-conn
          result (db/get-product-by-uuid! (shared/generate-uuid!) @database)]
      (is (empty? result))
      (is (true? (m/validate s/ProductQueryResult result))))))

(deftest products-get-product-by-slug-test
  (testing "Finds a product by it's slug"
    (let [u (create-product {:product/sku "unique-sku"
                             :product/slug "unique-slug"})
          database database-conn
          _ (db/create-product! u
                                @database)
          result (db/get-product-by-slug! (:product/slug u) @database)]
      (is (true? (m/validate s/ProductQueryResult result)))))
  (testing "Returns nil for a non-existent slug"
    (let [database database-conn
          result (db/get-product-by-slug! "non-existent-slug" @database)]
      (is (empty? result))
      (is (true? (m/validate s/ProductQueryResult result))))))
