(ns short.products.contracts-test
  (:require [short.products.contracts :as c]
            [malli.core :as ml]
            [short.shared :as shared]
            [clojure.test :refer [deftest testing is]]))

(deftest products-contract-productdata-test
  (testing "Matches the expected external input"
    (let [input {:sku "fasjfsaoij123"
                 :active true
                 :slug "some-slug"
                 :title "some-title"
                 :price 30
                 :quantity 2}]
      (is (true? (ml/validate c/ProductData input)))))
  (testing "Fails for an unexpected external input"
    (let [input {:sku "hi there"}]
      (is (false? (ml/validate c/ProductData input))))))

(deftest users-contract-productout-test
  (testing "Matches the expected external output"
    (let [output {:product/sku "fasjfsaoij123"
                  :product/active true
                  :product/slug "some-slug"
                  :product/title "some-title"
                  :product/price 30
                  :product/quantity 2
                  :product/uuid (shared/generate-uuid!)
                  :product/created_at (shared/get-current-inst!)}]
      (is (true? (ml/validate c/ProductOut output)))))
  (testing "Fails for an unexpected external output"
    (let [output {:product/sku "test"}]
      (is (false? (ml/validate c/ProductOut output))))))
