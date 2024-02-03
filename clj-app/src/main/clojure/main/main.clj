(ns main.main
  (:gen-class)
  (:import java.security.MessageDigest))

(defn hexdigest
  "Returns the hex digest of an object. Expects a string as input."
  ([input hash-algo]
   (if (string? input)
     (let [hash (MessageDigest/getInstance hash-algo)]
       (. hash update (.getBytes input))
       (let [digest (.digest hash)]
         (apply str (map #(format "%02x" (bit-and % 0xff)) digest))))
     (do
       (println "Invalid input! Expected string, got" (type input))
       nil))))

(defn sha256
  [input]
  (hexdigest input "SHA-256"))

(defn wax-lines-in-file
  [f]
  (with-open [rdr (clojure.java.io/reader f)]
    (->> rdr
         line-seq
         (filter #(.contains % "wax synthase"))
         doall)))

(defn wax-lines
  []
  (let [rootDir (clojure.java.io/file "../genomas")
        files   (filter #(.isFile %) (file-seq rootDir))
        lines   (->> files
                     (map wax-lines-in-file)
                     flatten
                     (clojure.string/join "\n"))]
    lines))

; fs (sha256 (.concat fs0 "\n"))

(defn -main
  [& args]
  (let [lines       (time (wax-lines))
        output_hash (sha256 (str lines "\n"))]
    (println output_hash)
    (assert
     (= output_hash "48aa9f97c5d4e0dd1dd275f4eaa941e282758b9a03e8c930c6850874284ebe5c"))))
