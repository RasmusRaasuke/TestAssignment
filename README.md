# Hindamisfailide Automaatne Uuendamine

## Sissejuhatus

See on minu lahendus antud ülesandele. Kirjutasin lahenduse Java-s nagu oli eelistuseks toodud.

## Kasutatud tehnoloogiad

Ülesande lahendamiseks kasustasin Java 23 ja Maven-it, et oleks lihtsam väliste teekidega majandada.

Kasutasin ühte välist teeki, milleks on [Apache POI](https://poi.apache.org/). Seda kasutasin, et saaksin **.docx** failidest lugdega ja sinna ka kirjutada.

## Lahendus

Lahendust saab jooksutada **Main.java** klassi tööle pannes.

Koodi käima pannes tuleb konsooli teade `main ERROR Log4j API could not find a logging provider.`. Midagi ei ole katki. Teek [Apache POI](https://poi.apache.org/) sõltub logimis teegist [Log4j API](https://logging.apache.org/log4j/2.x/manual/api.html) ja tahab, et seda kastutataks. Mina seda ei kasutanud.

### Üles laadimine

Oma lahenduses kasutaja "üles laadimist" näitan nii, et on muutuja **inputFilename**, millele saab anda test faili nime. Mõlemad nimed on olemas ka koodis (üks on välja kommenteeritud).

Päriselt kui tahaks seda lahendust kastuada näiteks dockeri konteineris või kuskil serveris, siis saaks selle naiivse lahendus asemel teha POST endpointi, mis võtab vastu faili.

### Teadmistebaasist info saamine

Esimese asajan kui kood tööle pannakse vaadatakse kõiki faile, mis on **/src/main/resources/database** kaustas ja neist loetakse sisse "andmebaas". Antud info on hoitud **../database/Database.java** klassis. Et kogu faili infot oleks lihtsam ühtselt edasi anda tegin ka **../database/DatabaseFail.java** klassi, mille ülesanne ongi hoida loetud faili sisu. Klass **../database/Database.java** hoiab Map-i, kus võtmeks on failist loetud esimene pealkiri ning väärtuseks on **../database/DatabaseFail.java** klass.

See lahendus eelab, et igas failis on ainult 1 pealkiri ja selle all ainut 1 paragraaf. Kuna mitte üheski näite failis teisiti ei olnud, siis tegin selle lihtsustuse.

Info lugemiseks failist tegin eraldi klassi **../file/DocxFileReader.java**. Seda kasutangi ainult andmete sisse lugemiseks "andmebaasi".

### Faili andmete uuendamine

Kui kastuaja on faili "üles laadinud", siis pannakse tööle klassi **../file/FileUpdater.java** `public void updateFileContents(String filename)` meetod. Hetkel võtab see meetod "üles laetud" faili **src/main/resources/input/** kasutast ja hakkab siis seda uuendama. Lahendus käib kõik paragraafid failist ükshaaval läbi. Kuna Peakiri failis on eraldi paragraaf, siis mu lahendus otsib failist paragraafi (pealkrirja), mis on olemas andmebaasis võtmena (sedasi leian lõigu, mida pean muutma). Kui on letiud pealkiri, mis on olemas andmebaasis, siis kontrollitakse, et järgmine paragraaf juba ei ole sama, mis andmebaasis on. Juhul kui sisu on erinev, siis järgmine paragraaf asendatakse sellega, mis on andmebaasis (eeldusel, et andmebaasis on alati kõige uuem info).

Kui toimub paragraafi muutmine, siis tehakse ka kirje log.txt faili, mis asub **/src/main/resources/log/** kasutas. (NB! Kasut ja fail tekivad peale koodi käivitamist.)

Kui terve "üles laetud" fail on läbi vaadatud, siis salvestatakse muudatustega fail **/src/main/resources/output/** kasusta. Faili nimi on sama, mis on algse faili nimi. (NB! Kaust ja fail tekivad peale koodi käivitamist.)
