<?php

/**
 *  Código retirado da documentação do php
 *  http://forum.imasters.com.br/topic/426836-resolvidoutf-8-ou-latin-1/
 *  Autor: Eduardo Giullyanny
 */
class Utf8Encoder
{
  public static function encode($in_str) // -- It returns $in_str encoded to UTF8
  {
    $cur_encoding = mb_detect_encoding($in_str) ;
    if($cur_encoding == "UTF-8" && mb_check_encoding($in_str,"UTF-8"))
      return $in_str;
    else
      return utf8_encode($in_str);

  }
}
?>
