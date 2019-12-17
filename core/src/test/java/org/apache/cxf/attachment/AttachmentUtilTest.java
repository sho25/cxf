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
name|attachment
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|AttachmentUtilTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameNoQuotes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"form-data; filename=a.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameNoQuotesAndType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename=a.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameNoQuotesAndType2
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"name=files; filename=a.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameSpacesNoQuotes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"form-data; filename = a.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameWithQuotes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"form-data; filename=\"a.txt\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameWithQuotesAndSemicolon
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a;txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"form-data; filename=\"a;txt\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameWithQuotesAndSemicolon2
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a;txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename=\"a;txt\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameWithQuotesAndSemicolon3
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a;txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"name=\"a\";filename=\"a;txt\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentDispositionAsterickMode
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a b.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename=\"bad.txt\"; filename*=UTF-8''a%20b.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentDispositionAsterickModeLowercase
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a b.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename*=utf-8''a%20b.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentDispositionAsterickModeFnUppercase
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a b.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"FILENAME*=utf-8''a%20b.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentDispositionFnUppercase
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a b.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"FILENAME=\"a b.txt\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameKanjiChars
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"世界ーファイル.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename*=UTF-8''%e4%b8%96%e7%95%8c%e3%83%bc%e3%83%95%e3%82%a1%e3%82%a4%e3%83%ab.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContendDispositionFileNameNoRfc5987
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"демо-сервис.zip"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename=\"&#1076;&#1077;&#1084;&#1086;-&#1089;&#1077;&#1088;&#1074;&#1080;&#1089;.zip\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentDispositionFnEquals
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a=b.txt"
argument_list|,
name|AttachmentUtil
operator|.
name|getContentDispositionFileName
argument_list|(
literal|"filename=\"a=b.txt\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateContentID
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|contentID
init|=
name|AttachmentUtil
operator|.
name|createContentID
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|contentID
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

