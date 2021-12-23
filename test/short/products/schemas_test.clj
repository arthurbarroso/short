(ns short.products.schemas-test
  (:require [short.products.schemas :as s]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]
            [short.shared :as shared]))

(deftest products-schema-product-test
  (testing "Matches the expected internal input"
    (let [input {:product/sku "sku"
                 :product/slug "slug"
                 :product/active true
                 :product/title "hithere"
                 :product/price 3
                 :product/created_at (shared/get-current-inst!)
                 :product/uuid (shared/generate-uuid!)}]
      (is (true? (ml/validate s/Product input)))))
  (testing "Fails for an unexpected internal input"
    (let [input {:product/sku "oi"}]
      (is (false? (ml/validate s/Product input))))))

(deftest products-schema-existingproduct-test
  (testing "Matches the expected internal product structure"
    (let [input {:product/sku "sku"
                 :product/slug "slug"
                 :product/active true
                 :product/title "hithere"
                 :product/price 3
                 :product/created_at (shared/get-current-inst!)
                 :product/uuid (shared/generate-uuid!)
                 :db/id 3}]
      (is (true? (ml/validate s/ExistingProduct input)))))
  (testing "Fails for an unexpected internal product structure"
    (let [input {:product/sku "s"}]
      (is (false? (ml/validate s/ExistingProduct input))))))

(deftest products-schema-productqueryresult-test
  (testing "Matches the expected internal product query result structure"
    (let [existing-product {:product/sku "sku"
                            :product/slug "slug"
                            :product/active true
                            :product/title "hithere"
                            :product/price 3
                            :product/created_at (shared/get-current-inst!)
                            :product/uuid (shared/generate-uuid!)
                            :db/id 3}
          result-with-product [[existing-product]]
          empty-result []]
      (is (true? (ml/validate s/ProductQueryResult result-with-product)))
      (is (true? (ml/validate s/ProductQueryResult empty-result)))))
  (testing "Fails for an unexpected internal product query result structure"
    (is (false? (ml/validate s/ProductQueryResult [[{:product/sku "s"}]])))))
