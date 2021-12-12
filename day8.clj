(ns day8
  (:require [clojure.set :as set]
            [clojure.string :as s])
  (:import (java.lang Integer)))

(def inputs
  ["be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
   "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc"
   "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg"
   "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb"
   "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea"
   "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb"
   "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe"
   "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef"
   "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb"
   "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"])

(comment
  (->> inputs
       (mapcat (fn [line]
                 (-> line
                     (s/split #"\|")
                     second
                     s/trim
                     (s/split #" "))))
       (map count)
       (filter #{2 4 3 7})
       count)

  (s/split (slurp "day8input.txt") #"\n")

  (->> (s/split (slurp "day8input.txt") #"\n")
       (mapcat (fn [line]
                 (-> line
                     (s/split #"\|")
                     second
                     s/trim
                     (s/split #" "))))
       (map count)
       (filter #{2 4 3 7})
       count))

;; part 2

;; I solved this a bit of an indirect way - using first the frequency of the segments
;; to get b, e and f's mapping. Then used the known digits to get the remaining segments

;; Letter count isn't used in the program - was part of working out the algorithm
(def letter-count
  {\a 8
   \b 6
   \c 8
   \d 7
   \e 4
   \f 9
   \g 7})

(defn get-translation-map [examples]
  (let [seg-freqs (-> (apply concat examples)
                      frequencies)
        b (ffirst (filter #(= 6 (second %)) seg-freqs))
        e (ffirst (filter #(= 4 (second %)) seg-freqs))
        f (ffirst (filter #(= 9 (second %)) seg-freqs))
        one (first (filter #(= 2 (count %)) examples))
        c (first (disj one f))
        seven (first (filter #(= 3 (count %)) examples))
        a (first (disj seven c f))
        four (first (filter #(= 4 (count %)) examples))
        d (first (disj four b c f))
        eight (first (filter #(= 7 (count %)) examples))
        g (first (disj eight a b c d e f))]
    (-> {}
        (assoc a \a)
        (assoc b \b)
        (assoc c \c)
        (assoc d \d)
        (assoc e \e)
        (assoc f \f)
        (assoc g \g))))

(def digit-lookup
  {#{\a \b \c \e \f \g} 0
   #{\c \f} 1
   #{\a \c \d \e \g} 2
   #{\a \c \d \f \g} 3
   #{\b \c \d \f} 4
   #{\a \b \d \f \g} 5
   #{\a \b \d \e \f \g} 6
   #{\a \c \f} 7
   #{\a \b \c \d \e \f \g} 8
   #{\a \b \c \d \f \g} 9})

(defn translate [wiring-map scrambled]
  (Integer/parseInt
    (apply str
           (map (fn [digit]
                  (->> digit
                       (map wiring-map)
                       set
                       digit-lookup
                       str))
                scrambled))))

(defn split-line [line]
  (->> (s/split line #"\|")
       (map (fn [side]
              (map set
                   (-> side
                       s/trim
                       (s/split #" ")))))))

(defn line->num [line]
  (let [[examples digits] (split-line line)
        t-map             (get-translation-map examples)]
    (translate t-map digits)))

(comment
  (def line "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe")

  (line->num line)

  (reduce #(+ %1 (line->num %2))
          0
          (s/split (slurp "day8input.txt") #"\n")))
