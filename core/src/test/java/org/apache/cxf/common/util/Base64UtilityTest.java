begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|Base64UtilityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEncodeMultipleChunks
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|text
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
comment|// multiple of 3 octets
name|assertEquals
argument_list|(
literal|63
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|off
init|=
literal|0
decl_stmt|;
for|for
control|(
init|;
name|off
operator|+
literal|21
operator|<
name|bytes
operator|.
name|length
condition|;
name|off
operator|+=
literal|21
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
literal|21
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|off
operator|<
name|bytes
operator|.
name|length
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|bytes
operator|.
name|length
operator|-
name|off
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|s2
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeAndStream
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|text
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|Base64Utility
operator|.
name|encodeAndStream
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|String
name|decodedText
init|=
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|decodedText
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeDecodeChunk
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bytes
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|bytes
index|[
name|x
index|]
operator|=
operator|(
name|byte
operator|)
name|x
expr_stmt|;
block|}
name|char
index|[]
name|encodedChars
init|=
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
operator|-
literal|2
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|encodedChars
argument_list|)
expr_stmt|;
name|encodedChars
operator|=
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|encodedChars
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytesDecoded
init|=
name|Base64Utility
operator|.
name|decodeChunk
argument_list|(
name|encodedChars
argument_list|,
literal|0
argument_list|,
name|encodedChars
operator|.
name|length
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|bytesDecoded
argument_list|)
expr_stmt|;
comment|//require padding
name|bytes
operator|=
operator|new
name|byte
index|[
literal|99
index|]
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bytes
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|bytes
index|[
name|x
index|]
operator|=
operator|(
name|byte
operator|)
name|x
expr_stmt|;
block|}
name|encodedChars
operator|=
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|encodedChars
argument_list|)
expr_stmt|;
name|bytesDecoded
operator|=
name|Base64Utility
operator|.
name|decodeChunk
argument_list|(
name|encodedChars
argument_list|,
literal|0
argument_list|,
name|encodedChars
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|bytesDecoded
argument_list|)
expr_stmt|;
comment|//require padding
name|bytes
operator|=
operator|new
name|byte
index|[
literal|98
index|]
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bytes
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|bytes
index|[
name|x
index|]
operator|=
operator|(
name|byte
operator|)
name|x
expr_stmt|;
block|}
name|encodedChars
operator|=
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|encodedChars
argument_list|)
expr_stmt|;
name|bytesDecoded
operator|=
name|Base64Utility
operator|.
name|decodeChunk
argument_list|(
name|encodedChars
argument_list|,
literal|0
argument_list|,
name|encodedChars
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|bytesDecoded
argument_list|)
expr_stmt|;
comment|//require padding
name|bytes
operator|=
operator|new
name|byte
index|[
literal|97
index|]
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bytes
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|bytes
index|[
name|x
index|]
operator|=
operator|(
name|byte
operator|)
name|x
expr_stmt|;
block|}
name|encodedChars
operator|=
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|encodedChars
argument_list|)
expr_stmt|;
name|bytesDecoded
operator|=
name|Base64Utility
operator|.
name|decodeChunk
argument_list|(
name|encodedChars
argument_list|,
literal|0
argument_list|,
name|encodedChars
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|bytesDecoded
argument_list|)
expr_stmt|;
name|bytesDecoded
operator|=
name|Base64Utility
operator|.
name|decodeChunk
argument_list|(
operator|new
name|char
index|[
literal|3
index|]
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|bytesDecoded
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeDecodeString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|in
init|=
literal|"QWxhZGRpbjpvcGVuIHNlc2FtZQ=="
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Aladdin:open sesame"
argument_list|,
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|encoded
init|=
name|Base64Utility
operator|.
name|encode
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|in
argument_list|,
name|encoded
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|Base64Exception
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testDecodeInvalidString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|in
init|=
literal|"QWxhZGRpbjpcGVuIHNlc2FtZQ=="
decl_stmt|;
name|Base64Utility
operator|.
name|decode
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeDecodeStreams
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bytes
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|bytes
index|[
name|x
index|]
operator|=
operator|(
name|byte
operator|)
name|x
expr_stmt|;
block|}
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bout2
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Base64Utility
operator|.
name|encodeChunk
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|,
name|bout
argument_list|)
expr_stmt|;
name|String
name|encodedString
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Base64Utility
operator|.
name|decode
argument_list|(
name|encodedString
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|encodedString
operator|.
name|length
argument_list|()
argument_list|,
name|bout2
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|bout2
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|in
init|=
literal|"QWxhZGRpbjpvcGVuIHNlc2FtZQ=="
decl_stmt|;
name|bout
operator|.
name|reset
argument_list|()
expr_stmt|;
name|bout2
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Base64Utility
operator|.
name|decode
argument_list|(
name|in
argument_list|,
name|bout
argument_list|)
expr_stmt|;
name|bytes
operator|=
name|bout
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Aladdin:open sesame"
argument_list|,
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Base64Utility
operator|.
name|encode
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// See https://tools.ietf.org/html/rfc4648#section-10
annotation|@
name|Test
specifier|public
name|void
name|testVectors
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|""
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Zg=="
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"f"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Zm8="
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"fo"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Zm9v"
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"foo"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Zm9vYg=="
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"foob"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Zm9vYmE="
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"fooba"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Zm9vYmFy"
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
literal|"foobar"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|""
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f"
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|"Zg=="
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fo"
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|"Zm8="
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|"Zm9v"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foob"
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|"Zm9vYg=="
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fooba"
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|"Zm9vYmE="
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
operator|new
name|String
argument_list|(
name|Base64Utility
operator|.
name|decode
argument_list|(
literal|"Zm9vYmFy"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

