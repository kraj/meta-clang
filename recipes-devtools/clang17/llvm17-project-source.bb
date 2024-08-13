# Copyright (C) 2018 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "This is the canonical git mirror of the LLVM subversion repository."
HOMEPAGE = "https://github.com/llvm/llvm-project"

require llvm-project-source.inc
require clang.inc

EXCLUDE_FROM_WORLD = "1"
