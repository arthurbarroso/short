(ns short.variants.logic-test
  (:require [short.variants.logic :as l]
            [short.variants.contracts :as c]
            [short.variants.schemas :as s]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]
            [short.shared :as shared]))

(deftest variants-logic-internal->external-test
  (testing "Dissociates the internal-only fields of a variant"
    (let [uuid (shared/generate-uuid!)
          now (shared/get-current-inst!)
          base-variant {:variant/quantity 3
                        :variant/active true
                        :variant/type "123super-secret"
                        :variant/image-url "123super-secret"
                        :variant/created_at now
                        :variant/uuid uuid
                        :variant/product {:product "oi"}
                        :db/id 123}
          result (l/internal->external base-variant)]
      (is (= {:variant/quantity 3
              :variant/active true
              :variant/type "123super-secret"
              :variant/image-url "123super-secret"
              :variant/created_at now
              :variant/uuid uuid
              :variant/product {:product "oi"}}
             result))
      (is (true? (ml/validate
                  c/VariantOut
                  result))))))

(deftest variants-variant-creation-test
  (testing "Builds a map that conforms the s/Variant schema"
    (let [base-variant {:active true
                        :quantity 3
                        :type "test"
                        :image-url "hi"}
          uuid (shared/generate-uuid!)
          now (shared/get-current-inst!)
          product {:product "hi"}
          result (l/variant-creation base-variant product uuid now)]
      (is (= result
             {:variant/type (:type base-variant)
              :variant/image-url (:image-url base-variant)
              :variant/uuid uuid
              :variant/active true
              :variant/created_at now
              :variant/product product
              :variant/quantity (:quantity base-variant)}))
      (is (true? (ml/validate
                  s/Variant
                  result))))))
