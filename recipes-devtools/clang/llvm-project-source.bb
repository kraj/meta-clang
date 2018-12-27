# Copyright (C) 2018 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Flat monorepo imported from http://llvm.org/git/ (17 repos)"
HOMEPAGE = "https://github.com/llvm-project/llvm-project-20170507"

require llvm-project-source.inc
require clang.inc

EXCLUDE_FROM_WORLD = "1"
