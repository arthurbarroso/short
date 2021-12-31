(ns short.shared-test
  (:require [short.shared :as s]
            [clojure.test :refer [deftest testing is]]
            [malli.core :as ml]))

(deftest shared-transacton-schema-test
  (testing "Matches the expected internal transaction format"
    (let [tx {:db-before {:tx-id 2}
              :db-after {:tx-id 3}
              :tx-data {:something "something"}
              :tempids {:first 1}}]
      (is (true? (ml/validate s/Transaction tx))))))

(deftest shared-tempid->eid-test
  (testing "Extracts the eid from a tempid map"
    (let [tx {:db-before {:tx-id 2}
              :db-after {:tx-id 3}
              :tx-data {:something "something"}
              :tempids {-1 2}}
          result (s/tempid->eid tx)]
      (is (= result 2)))))

(deftest shared-generate-uuid-test
  (testing "Generates a valid uuid"
    (let [result (s/generate-uuid!)]
      (is (true? (uuid? result))))))

(deftest shared-uuid-from-string-test
  (testing "Creates a valid uuid from a valid uuid string"
    (let [uuid-string (str (s/generate-uuid!))
          result (s/uuid-from-string uuid-string)]
      (is (true? (uuid? result))))))

(deftest shared-get-current-inst-test
  (testing "Creates a valid `inst`"
    (let [result (s/get-current-inst!)]
      (is (true? (inst? result))))))
