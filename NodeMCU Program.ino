#include <SPI.h>  //SPI BUS
#include <MFRC522.h>//RFID READER

#include "FirebaseESP8266.h"//Firebase Database
#include <ESP8266WiFi.h>//Wifi module

#define FIREBASE_HOST "smartmealsolution-default-rtdb.firebaseio.com"//Database URL
#define FIREBASE_AUTH "btk0TIY90B0JYH7SBuy2pmdLh5TozNzJqGEsGPdT"//Database secrate
#define WIFI_SSID "Dining"//Wifi name
#define WIFI_PASSWORD "dining123"//Wifi pass

FirebaseData firebaseData,loadData;//Object veriable
FirebaseJson json;//Object veriable


MFRC522 mfrc522(D4, D2);//RFID Pin set

String _name[4] = {"Najnin", "Zainal", "Sojib", "Shanjana"};

int dn=1,dd = 5, mm = 9, yy = 2021;

int mealRate=0;


bool loadR(String field){
 
if (Firebase.getInt(loadData, "/Users/"+field+"/"+(dn==1?"Meal1":"Meal2"))){
    return loadData.intData()==0?false:true;
  }
}

int loadMeal(String field){
  if (Firebase.getInt(loadData, "/Users/"+field+"/MealCount")){
    return loadData.intData();
  }else return 0;
}
int loadBalance(String field){
  if (Firebase.getString(loadData, "/Users/"+field+"/Balance")){
    return (loadData.stringData()).toInt();
  }else return 0;
}

int loadMealrate(){
  if (Firebase.getInt(loadData, "/MealRate")){
    return loadData.intData();
  }else return 0;
}



void writeDB(String field) {

  int count=loadMeal(field);
  int balance=loadBalance(field);
  mealRate=loadMealrate();

   Serial.print("Count: ");
   Serial.println(count);
   Serial.print("Mealrate: ");
   Serial.println(mealRate);
   Serial.print("Balance: ");
   Serial.println(balance);
   Serial.print("Total Cost: ");
   Serial.println(mealRate*(count+1));
   
   
   

  if((balance-(mealRate*count))<50){ //Checking balance and purchase meal
    insBalError();
    return;
  }

  if(loadR(field)){ 
  Firebase.setString(firebaseData, "/MealList/" + field + "/" + String(dd) + "-" + String(mm) + "-" + String(yy)+" "+(dn==1?"Day":"Night"), "1" );
  Firebase.setInt(firebaseData, "/Users/"+field+"/MealCount", count+1 );
  Serial.println("Attendence with " + field);
  }
  else{
    error();
  }


}










void inc() {


  Serial.println("Increment");
  dn++;
  Serial.println(dn);
  if(dn>2){
    dd++;
    dn=1;
  }
  if (dd > 30)
  {
    mm++;
    dd = 1;
  }

  if (mm > 12) {
    yy++;
    mm = 1;
  }

  digitalWrite(D0, 1);
  delay(500);
  digitalWrite(D0, 0);

}


int checkCard() {


  // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent())
  {
    return -1;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial())
  {
    return -1;
  }
  //Show UID on serial monitor
  //Serial.print("UID tag :");
  String content = "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++)
  {
    //Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
    //Serial.print(mfrc522.uid.uidByte[i], HEX);
    content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
    content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }

  delay(500);
  content.toUpperCase();
  Serial.print("Card serial: ");
  Serial.println(content.substring(1));

  if (content.substring(1) == "D3 D7 99 1C") //change here the UID of the card/cards that you want to give access
  {
    beep();
    return 1;
  }

  else if (content.substring(1) == "D3 91 43 1C") {
    beep();
    return 2;
  }
  else if (content.substring(1) == "D3 A1 85 1C") {
    beep();
    return 3;
  }
  else if (content.substring(1) == "D3 82 60 1C") {
    beep();
    return 4;
  }


  return -1;
}


void beep() {
  digitalWrite(D0, 1);
  delay(100);
  digitalWrite(D0, 0);
  delay(50);
  digitalWrite(D0, 1);
  delay(100);
  digitalWrite(D0, 0);

}


void error() {
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  delay(100);
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  delay(100);
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  

}


void insBalError() {
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  delay(100);
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  delay(100);
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  delay(100);
  digitalWrite(D0, 1);
  delay(200);
  digitalWrite(D0, 0);
  

}







void t1(){
  Serial.begin(9600);    // Initialize serial communications with the PC
  while (!Serial);    // Do nothing if no serial port is opened (added for Arduinos based on ATMEGA32U4)
  SPI.begin();      // Init SPI bus
  mfrc522.PCD_Init();   // Init MFRC522
  delay(4);       // Optional delay. Some board do need more time after init to be ready, see Readme
  mfrc522.PCD_DumpVersionToSerial();  // Show details of PCD - MFRC522 Card Reader details
  Serial.println(F("Scan PICC to see UID, SAK, type, and data blocks..."));
}


void t2(){
    if ( ! mfrc522.PICC_IsNewCardPresent()) {
    return;
  }

  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return;
  }

  // Dump debug info about the card; PICC_HaltA() is automatically called
  mfrc522.PICC_DumpToSerial(&(mfrc522.uid));
}























void setup() {
t1();
  pinMode(D0, OUTPUT);
  pinMode(D8, INPUT);
  Serial.begin(9600); 
  SPI.begin();
  mfrc522.PCD_Init();

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED)
  {
    digitalWrite(D4, 0);
    delay(100);
    Serial.print(".");
    digitalWrite(D4, 1);
    delay(100);
  }

  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

   Firebase.setString(firebaseData, "Testing", "1" );
}

void loop() {

//t2();
  if (digitalRead(D8))//Date change

  {
    inc();
    while (digitalRead(D8))
      delay(50);
    Serial.println("In inf");

    return;
  }


  int i = checkCard();
  Serial.println(i);
  if(i>0 && i<5)
    writeDB(_name[i-1]);

  delay(300);

}
