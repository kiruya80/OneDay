package com.ulling.lib.core.model;

/**
 *
 *
 * DataModel  — 데이터 소스를 추상화합니다.
 * DataModel은 데이터를 이벤트 스트림을 통해서 소비 가능하게(consumable) 노출시킵니다
 *
 * 즉,
 * 네트워크 계층이나 데이터베이스 또는 shared preferences 등의 다양한 소스로 부터 데이터를 구성합니다.
 * 그리고 쉽게 소비가능한 데이터를 누구든지 필요한 것들에 노출시킵니다.
 * DataModel은 모든 비지니스 로직을 가지고 있게 됩니다.
 *
 * ex) 출력값을 API 서비스와 데이터베이스 계층으로부터 받아와 구성하는 ArticleDataModel이 있다고 합시다.
 * 이 DataModel은 age filter를 적용하여 최근의 뉴스들이 데이터베이스로부터 받아지도록 하기 위해서 비지니스 로직을 다루게 됩니다.
 *
 *
 */
public class QcBaseDataModel {
}
