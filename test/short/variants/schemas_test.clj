(ns short.variants.schemas-test
  (:require [short.variants.schemas :as s]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]
            [short.shared :as shared]))

(deftest variants-schema-variant-test
  (testing "Matches the expected internal input"
    (let [input {:variant/uuid (shared/generate-uuid!)
                 :variant/active true
                 :variant/quantity 3
                 :variant/type "hithere"
                 :variant/image-url "hithere"
                 :variant/created_at (shared/get-current-inst!)
                 :variant/product {:a-product "right"}}]
      (is (true? (ml/validate s/Variant input)))))
  (testing "Fails for an unexpected internal input"
    (let [input {:type "oi"}]
      (is (false? (ml/validate s/Variant input))))))

(deftest variants-schema-existingvariant-test
  (testing "Matches the expected internal variant structure"
    (let [input {:variant/uuid (shared/generate-uuid!)
                 :variant/active true
                 :variant/quantity 3
                 :variant/type "hithere"
                 :variant/image-url "hithere"
                 :variant/created_at (shared/get-current-inst!)
                 :variant/product {:a-product "right"}
                 :db/id 3}]
      (is (true? (ml/validate s/ExistingVariant input)))))
  (testing "Fails for an unexpected internal variant structure"
    (let [input {:variant/quantity 1}]
      (is (false? (ml/validate s/ExistingVariant input))))))
