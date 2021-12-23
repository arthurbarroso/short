(ns short.variants.db-test
  (:require [short.variants.db :as db]
            [short.products.db :as products-db]
            [short.variants.schemas :as s]
            [malli.core :as m]
            [test-helpers :as th]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.shared :as shared]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "variants-tests"))
    (f)))

(defn create-variant [opts]
  (merge
   {:variant/uuid (shared/generate-uuid!)
    :variant/active true
    :variant/quantity 2
    :variant/type "some-slug"
    :variant/image-url "some-title"
    :variant/created_at (shared/get-current-inst!)
    :variant/product {:product "a"}}
   opts))

(deftest variants-create-variant-test
  (testing "Creates a new variant"
    (let [database database-conn
          vuid (shared/generate-uuid!)
          pdid (shared/generate-uuid!)
          pd {:product/sku "fasjfsaoij123"
              :product/active true
              :product/slug "some-slug"
              :product/title "some-title"
              :product/price (bigdec 30)
              :product/uuid pdid
              :product/created_at (shared/get-current-inst!)}
          _ (products-db/create-product! pd @database)
          result (db/create-variant! (create-variant
                                      {:variant/product [:product/uuid pdid]
                                       :variant/uuid vuid})
                                     @database)]
      (is (not (nil? (:tx-data result))))
      (is (true? (m/validate shared/Transaction result))))))

(deftest procuts-get-product-test
  (testing "Finds a product"
    (let [database database-conn
          pdid (shared/generate-uuid!)
          pd {:product/sku "fasjfsaoij123-test1"
              :product/active true
              :product/slug "some-slug-test1"
              :product/title "some-title-test1"
              :product/price (bigdec 30)
              :product/uuid pdid
              :product/created_at (shared/get-current-inst!)}
          _ (products-db/create-product! pd @database)
          varianttx (db/create-variant! (create-variant
                                         {:variant/product
                                          [:product/uuid pdid]})
                                        @database)
          eid (shared/tempid->eid varianttx)
          result (db/get-variant! eid @database)]
      (is (true? (m/validate s/ExistingVariant result))))))

(deftest variants-get-variant-by-uuid-test
  (testing "Finds a variant by it's uuid"
    (let [database database-conn
          vuid (shared/generate-uuid!)
          pdid (shared/generate-uuid!)
          pd {:product/sku "fasjfsaoij123-test"
              :product/active true
              :product/slug "some-slug-test"
              :product/title "some-title-test"
              :product/price (bigdec 30)
              :product/uuid pdid
              :product/created_at (shared/get-current-inst!)}
          _ (products-db/create-product! pd @database)
          _1 (db/create-variant! (create-variant
                                  {:variant/product [:product/uuid pdid]
                                   :variant/uuid vuid})
                                 @database)
          result (db/get-variant-by-uuid! vuid @database)]
      (is (true? (m/validate s/VariantQueryResult result)))))
  (testing "Returns nil for a non-existent uuid"
    (let [database database-conn
          result (db/get-variant-by-uuid! (shared/generate-uuid!) @database)]
      (is (empty? result))
      (is (true? (m/validate s/VariantQueryResult result))))))
