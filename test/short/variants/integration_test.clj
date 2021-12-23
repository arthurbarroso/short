(ns short.variants.integration-test
  (:require [test-helpers :as th]
            [malli.core :as ml]
            [clojure.instant :as instant]
            [clojure.test :refer [deftest testing is]]
            [short.users.handlers :refer [gen-token!]]
            [short.products.handlers :as ph]
            [short.variants.contracts :as c]
            [short.shared :as shared]))

(deftest variants-integration-creation-test
  (testing "Creates a variant"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          product (ph/create-product!
                   {:sku "fasjfsaoij123331"
                    :active true
                    :slug "some-slug31"
                    :title "some-title31"
                    :price 30} (th/database-atom))
          {:keys [status body]}
          (th/endpoint-test :post (str "/v1/variants/" (:product/uuid product))
                            {:body {:active true
                                    :type "some-type"
                                    :image-url "some-image-url"
                                    :quantity 3}
                             :auth {:token (:token token)}})
          parsed-res {:variant/uuid (shared/uuid-from-string (:variant/uuid body))
                      :variant/active (:variant/active body)
                      :variant/quantity (:variant/quantity body)
                      :variant/type (:variant/type body)
                      :variant/product (:variant/product body)
                      :variant/created_at (instant/read-instant-date
                                           (:variant/created_at body))
                      :variant/image-url (:variant/image-url body)}]
      (is (= 201 status))
      (is (true? (ml/validate c/VariantOut parsed-res)))))
  (testing "Fails for a non-existent product uuid"
    (let [token (gen-token! {:matches? true
                             :existing-user {:email "arthur@test.com"}}
                            {:auth {:jwt-secret "test"}})
          {:keys [status _body]}
          (th/endpoint-test :post (str "/v1/variants/" (shared/generate-uuid!))
                            {:body {:active true
                                    :type "some-type"
                                    :image-url "some-image-url"
                                    :quantity 3}
                             :auth token})]
      (is (= 400 status))))
  (testing "Fails for an unauthenticated request"
    (let [{:keys [status _body]}
          (th/endpoint-test :post (str "/v1/variants/"
                                       (shared/generate-uuid!))
                            {:body {:active true
                                    :type "some-type"
                                    :image-url "some-image-url"
                                    :quantity 3}})]
      (is (= 401 status)))))
