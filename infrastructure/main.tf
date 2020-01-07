# Temporary fix for template API version error on deployment
provider "azurerm" {
  version = "1.22.0"
}

locals {
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  preview_app_service_plan = "${var.product}-${var.component}-${var.env}"
  non_preview_app_service_plan = "${var.product}-${var.env}"
  app_service_plan = "${var.env == "preview" || var.env == "spreview" ? local.preview_app_service_plan : local.non_preview_app_service_plan}"

  preview_vault_name = "${var.raw_product}-aat"
  non_preview_vault_name = "${var.raw_product}-${var.env}"
  key_vault_name = "${var.env == "preview" || var.env == "spreview" ? local.preview_vault_name : local.non_preview_vault_name}"

}

data "azurerm_key_vault" "rd_key_vault" {
  name = "${local.key_vault_name}"
  resource_group_name = "${local.key_vault_name}"
}

data "azurerm_key_vault_secret" "ACCOUNT_NAME" {
  name = "ACCOUNT-NAME"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "ACCOUNT_KEY" {
  name = "ACCOUNT-KEY"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "CONTAINER_NAME" {
  name = "CONTAINER-NAME"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "BLOB_URL_SUFFIX" {
  name = "BLOB-URL-SUFFIX"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "SFTP_USER_NAME" {
  name = "SFTP-USER-NAME"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "SFTP_USER_PASSWORD" {
  name = "SFTP-USER-PASSWORD"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "SFTP_HOST" {
  name = "SFTP-HOST"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "SFTP_FILE" {
  name = "SFTP-FILE"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "GPG_PASSWORD" {
  name = "GPG-PASSWORD"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "GPG_PUBLIC_KEY" {
  name = "GPG-PUBLIC-KEY"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

data "azurerm_key_vault_secret" "GPG_PRIVATE_KEY" {
  name = "GPG-PRIVATE-KEY"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name      = "${var.component}-POSTGRES-USER"
  value     = "${module.db-judicial-ref-data.user_name}"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name      = "${var.component}-POSTGRES-PASS"
  value     = "${module.db-judicial-ref-data.postgresql_password}"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name      = "${var.component}-POSTGRES-HOST"
  value     = "${module.db-judicial-ref-data.host_name}"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name      = "${var.component}-POSTGRES-PORT"
  value     = "5432"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name      = "${var.component}-POSTGRES-DATABASE"
  value     = "${module.db-judicial-ref-data.postgresql_database}"
  key_vault_id = "${data.azurerm_key_vault.rd_key_vault.id}"
}

resource "azurerm_resource_group" "rg" {
  name = "${var.product}-${var.component}-${var.env}"
  location = "${var.location}"
  tags {
    "Deployment Environment" = "${var.env}"
    "Team Name" = "${var.team_name}"
    "lastUpdated" = "${timestamp()}"
  }
}



module "db-judicial-ref-data" {
  source = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  product = "${var.product}-${var.component}-postgres-db"
  location = "${var.location}"
  subscription = "${var.subscription}"
  env = "${var.env}"
  postgresql_user = "dbjuddata"
  database_name = "dbjuddata"
  common_tags = "${var.common_tags}"
}

module "rd_judicial_data_load" {
  source = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product = "${var.product}-${var.component}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  subscription = "${var.subscription}"
  capacity = "${var.capacity}"
  instance_size = "${var.instance_size}"
  common_tags = "${merge(var.common_tags, map("lastUpdated", "${timestamp()}"))}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  asp_name = "${local.app_service_plan}"
  asp_rg = "${local.app_service_plan}"

  app_settings = {
    LOGBACK_REQUIRE_ALERT_LEVEL = false
    LOGBACK_REQUIRE_ERROR_CODE = false

    POSTGRES_HOST = "${module.db-judicial-ref-data.host_name}"
    POSTGRES_PORT = "${module.db-judicial-ref-data.postgresql_listen_port}"
    POSTGRES_DATABASE = "${module.db-judicial-ref-data.postgresql_database}"
    POSTGRES_USER = "${module.db-judicial-ref-data.user_name}"
    POSTGRES_USERNAME = "${module.db-judicial-ref-data.user_name}"
    POSTGRES_PASSWORD = "${module.db-judicial-ref-data.postgresql_password}"
    POSTGRES_CONNECTION_OPTIONS = "?"


    ROOT_LOGGING_LEVEL = "${var.root_logging_level}"
    LOG_LEVEL_SPRING_WEB = "${var.log_level_spring_web}"
    LOG_LEVEL_RD = "${var.log_level_rd}"
    EXCEPTION_LENGTH = 100
  }
}
