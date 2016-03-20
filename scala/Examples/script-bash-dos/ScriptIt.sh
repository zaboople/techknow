#!/bin/bash
scala "$0" "$@"
!#
object HelloWorld extends App {
  println("Hello, world!")
}
HelloWorld.main(args)