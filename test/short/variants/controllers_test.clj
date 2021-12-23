(ns short.variants.controllers-test
  (:require [short.variants.controllers :as cont]
            [test-helpers :as th]
            [malli.core :as ml]
            [short.products.handlers :as ph]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.variants.contracts :as c]
            [short.shared :as shared]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db
                           "variants-tests-controllers"))
    (f)))

(deftest products-create-product-controller-test
  (testing "Creates a new product"
    (let [database database-conn
          product (ph/create-product!
                   {:sku "fasjfsaoij123331"
                    :active true
                    :slug "some-slug31"
                    :title "some-title31"
                    :price 30} @database)
          {:keys [status body]}
          ((cont/create-variant-controller! @database)
           {:parameters {:body {:active true
                                :quantity 4
                                :type "type"
                                :image-url "image"}
                         :path {:product-id (str (:product/uuid product))}}})]
      (is (= 201 status))
      (is (true? (ml/validate c/VariantOut body)))))
  (testing "Fails to create a new variant when the product-id is invalid"
    (let [database database-conn
          {:keys [status _body]}
          ((cont/create-variant-controller! @database)
           {:parameters {:body {:active true
                                :quantity 4
                                :type "type"
                                :image-url "image"}
                         :path {:product-id (str (shared/generate-uuid!))}}})]
      (is (= 400 status)))))
