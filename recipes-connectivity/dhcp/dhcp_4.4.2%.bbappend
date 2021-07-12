# SPDX-FileCopyrightText: Huawei Inc.
# SPDX-License-Identifier: Apache-2.0

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = "\
    file://dhcp-4.4.2-clang-12.patch \
"
