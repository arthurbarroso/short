(ns short.products.logic-test
  (:require [short.products.logic :as l]
            [short.products.schemas :as s]
            [short.products.contracts :as c]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]
            [short.shared :as shared]))

(deftest procuts-tx->id-test
  (testing "Extracts tempids from a transaction"
    (let [tx {:tempids {1 2}
              :db-before {}
              :db-after {}
              :tx-data {}}
          result (l/tx->id tx)]
      (is (= 2 result)))))

(deftest procuts-internal->external-test
  (testing "Dissociates the internal-only fields of a product"
    (let [uuid (shared/generate-uuid!)
          now (shared/get-current-inst!)
          base-product {:product/sku "fasjfsaoij123"
                        :product/active true
                        :product/slug "some-slug"
                        :product/title "some-title"
                        :product/price 30
                        :db/id 313
                        :product/quantity 2
                        :product/uuid uuid
                        :product/created_at now}
          result (l/internal->external base-product)]
      (is (=
           {:product/sku "fasjfsaoij123"
            :product/active true
            :product/slug "some-slug"
            :product/title "some-title"
            :product/price 30
            :product/quantity 2
            :product/uuid uuid
            :product/created_at now}
           result))
      (is (true? (ml/validate
                  c/ProductOut
                  result))))))

(deftest products-product-creation-test
  (testing "Builds a map that conforms the s/Product schema"
    (let [base-product {:sku "fasjfsaoij123"
                        :active true
                        :slug "some-slug"
                        :title "some-title"
                        :price 30
                        :id 313
                        :quantity 2}
          uuid (shared/generate-uuid!)
          now (shared/get-current-inst!)
          result (l/product-creation base-product uuid now)]
      (is (= result
             {:product/sku "fasjfsaoij123"
              :product/active true
              :product/slug "some-slug"
              :product/title "some-title"
              :product/price 30
              :product/quantity 2
              :product/uuid uuid
              :product/created_at now}))
      (is (true? (ml/validate
                  s/Product
                  result))))))
