(ns short.products.integration-test
  (:require [test-helpers :as th]
            [short.products.contracts :as c]
            [malli.core :as ml]
            [clojure.instant :as instant]
            [clojure.test :refer [deftest testing is]]
            [short.users.handlers :refer [gen-token!]]))

(defn product-body [opts]
  (merge
   {:sku "fasjfsaoij123skulegal"
    :active true
    :slug "some-slug-sku-legal"
    :title "some-title-legal"
    :price 30}
   opts))

(deftest products-integration-creation-test
  (testing "Creates a product"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          {:keys [status body]}
          (th/endpoint-test :post "/v1/products"
                            {:body (product-body {})
                             :auth {:token (:token token)}})
          parsed-res {:product/sku (:product/sku body)
                      :product/active (:product/active body)
                      :product/slug (:product/slug body)
                      :product/title (:product/title body)
                      :product/price (:product/price body)
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
                            {:body (product-body {})
                             :auth token})]
      (is (= 400 status))))
  (testing "Fails for an unauthenticated request"
    (let [{:keys [status _body]}
          (th/endpoint-test :post "/v1/products"
                            {:body (product-body {})})]
      (is (= 401 status)))))

(deftest products-integration-render-test
  (testing "Renders an existing product"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          _ (th/endpoint-test :post "/v1/products"
                              {:body (product-body {:slug "render-testing-slug"
                                                    :sku "render-testing-sku"})
                               :auth {:token (:token token)}})
          {:keys [status _body]}
          (th/endpoint-test :get "/v1/products/render-testing-slug"
                            {:body (product-body {})
                             :auth {:token (:token token)}
                             :html true})]
      (is (= 200 status)))))

(deftest products-integration-list-test
  (testing "Lists products"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          _ (th/endpoint-test :post "/v1/products"
                              {:body (product-body {:slug "testing-slug-list"
                                                    :sku "testing-sku-list"})
                               :auth {:token (:token token)}})
          {:keys [status body]}
          (th/endpoint-test :get "/v1/products"
                            {:auth {:token (:token token)}})]
      (is (= 200 status))
      (is (> (count body) 0))))
  (testing "Fails for an unauthenticated request"
    (let [{:keys [status _body]}
          (th/endpoint-test :get "/v1/products")]
      (is (= 401 status)))))

(deftest products-integration-update-test
  (testing "Updates an existing product"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          product (th/endpoint-test :post "/v1/products"
                                    {:body (product-body {:slug "update-testing"
                                                          :sku "update-testing"})
                                     :auth {:token (:token token)}})
          {:keys [status _body]}
          (th/endpoint-test :put (str "/v1/products/update/" (-> product :body :product/uuid))
                            {:body (product-body {:sku "update-thinggyyy"
                                                  :slug "updatedthings"})
                             :auth {:token (:token token)}})]

      (is (= 200 status)))))
