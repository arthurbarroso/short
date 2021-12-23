(ns short.variants.contracts-test
  (:require [short.variants.contracts :as c]
            [malli.core :as ml]
            [short.shared :as shared]
            [clojure.test :refer [deftest testing is]]))

(deftest variants-contract-variantdata-test
  (testing "Matches the expected external input"
    (let [input {:active true
                 :quantity 2
                 :type "some-type"
                 :image-url "some-img-url"}]
      (is (true? (ml/validate c/VariantData input)))))
  (testing "Fails for an unexpected external input"
    (let [input {:type "hi there"}]
      (is (false? (ml/validate c/VariantData input))))))

(deftest variant-contract-variantout-test
  (testing "Matches the expected external output"
    (let [output {:variant/type "fasjfsaoij123"
                  :variant/active true
                  :variant/image-url "some-img-url"
                  :variant/quantity 2
                  :variant/uuid (shared/generate-uuid!)
                  :variant/created_at (shared/get-current-inst!)
                  :variant/product {:some-product "i dont care about yet"}}]
      (is (true? (ml/validate c/VariantOut output)))))
  (testing "Fails for an unexpected external output"
    (let [output {:variant/type "test"}]
      (is (false? (ml/validate c/VariantOut output))))))
