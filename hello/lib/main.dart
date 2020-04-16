import 'package:flutter/material.dart';
import 'package:hello/first_screen.dart';

void main()=>runApp(new MyFlutterApp());

class MyFlutterApp extends StatelessWidget
{
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
        title: "My flutter App",
        debugShowCheckedModeBanner: false,
        home:Scaffold(
          appBar: AppBar(title: Text("My first program"),),
          body: Firstscreen()
        )
    );
  }

}