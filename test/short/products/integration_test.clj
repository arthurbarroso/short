(ns short.products.integration-test
  (:require [test-helpers :as th]
            [short.products.contracts :as c]
            [malli.core :as ml]
            [clojure.instant :as instant]
            [clojure.test :refer [deftest testing is]]
            [short.users.handlers :refer [gen-token!]]))

(def product-body
  {:sku "fasjfsaoij123skulegal"
   :active true
   :slug "some-slug-sku-legal"
   :price 30
   :quantity 2})

(deftest products-integration-creation-test
  (testing "Creates a product"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          {:keys [status body]}
          (th/endpoint-test :post "/v1/products"
                            {:body product-body
                             :auth {:token (:token token)}})
          parsed-res {:product/sku (:product/sku body)
                      :product/active (:product/active body)
                      :product/slug (:product/slug body)
                      :product/price (:product/price body)
                      :product/quantity (:product/quantity body)
                      :product/uuid (java.util.UUID/fromString
                                     (:product/uuid body))
                      :product/created_at (instant/read-instant-date
                                           (:product/created_at body))}]
      (is (= 201 status))
      (is (true? (ml/validate c/ProductOut parsed-res)))))
  (testing "Fails for an already registered slug"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          {:keys [status _body]}
          (th/endpoint-test :post "/v1/products"
                            {:body product-body
                             :auth token})]
      (is (= 400 status))))
  (testing "Fails for an unauthenticated request"
    (let [{:keys [status _body]}
          (th/endpoint-test :post "/v1/products"
                            {:body product-body})]
      (is (= 401 status)))))
