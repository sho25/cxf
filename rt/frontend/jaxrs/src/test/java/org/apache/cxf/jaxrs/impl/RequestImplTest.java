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
name|jaxrs
operator|.
name|impl
package|;
end_package

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
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|EntityTag
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Variant
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|RequestImplTest
block|{
specifier|private
name|Message
name|m
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|metadata
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|m
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|metadata
operator|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearUp
parameter_list|()
block|{
name|m
operator|=
literal|null
expr_stmt|;
name|metadata
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleMatchingVariant
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
literal|null
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleMatchingVariantWithContentTypeOnly
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
literal|null
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameVariant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleNonMatchingVariant
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleNonMatchingVariants
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"es"
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleVariantsSingleMatch
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"es"
argument_list|)
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
expr_stmt|;
name|Variant
name|var3
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var3
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var3
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleVariantsBestMatch
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en-us"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
literal|"gzip;q=1.0, compress"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
argument_list|)
expr_stmt|;
name|Variant
name|var2
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var2
argument_list|)
expr_stmt|;
name|Variant
name|var3
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var3
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var2
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var3
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var3
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleVariantsBestMatchMediaTypeQualityFactors
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
literal|"a/b;q=0.6, c/d;q=0.5, e/f+json"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en-us"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
literal|"gzip;q=1.0, compress"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Variant
name|var1
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"a/b"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
decl_stmt|;
name|Variant
name|var2
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"x/z"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
decl_stmt|;
name|Variant
name|var3
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"e/f+json"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
decl_stmt|;
name|Variant
name|var4
init|=
operator|new
name|Variant
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"c/d"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"gzip"
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var2
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var3
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var4
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var3
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var4
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var1
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var2
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var4
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var4
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSameVariant
parameter_list|(
name|MediaType
name|mt
parameter_list|,
name|Locale
name|lang
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
name|Variant
name|var
init|=
operator|new
name|Variant
argument_list|(
name|mt
argument_list|,
name|lang
argument_list|,
name|enc
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|var
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|var
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|selectVariant
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWeakEtags
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Match"
argument_list|,
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|,
literal|true
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Strict compararison is required"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|rb
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"If-Match precondition was not met"
argument_list|,
literal|412
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Response should include ETag"
argument_list|,
literal|"\"123\""
argument_list|,
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"ETag"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMethod
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Wrong method"
argument_list|,
literal|"GET"
argument_list|,
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStrictEtagsPreconditionMet
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Match"
argument_list|,
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Precondition must be met"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStrictEtagsPreconditionNotMet
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Match"
argument_list|,
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|,
literal|true
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition must not be met, strict comparison is required"
argument_list|,
literal|412
argument_list|,
name|rb
operator|.
name|build
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStarEtags
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Match"
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Precondition must be met"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStarEtagsIfNotMatch
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition must not be met"
argument_list|,
literal|304
argument_list|,
name|rb
operator|.
name|build
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIfNotMatchAndLastModified
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|,
operator|new
name|EntityTag
argument_list|(
literal|"1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition must not be met"
argument_list|,
literal|304
argument_list|,
name|rb
operator|.
name|build
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEtagsIfNotMatch
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
literal|"\"123\""
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition must not be met"
argument_list|,
literal|304
argument_list|,
name|rb
operator|.
name|build
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStarEtagsIfNotMatchPut
parameter_list|()
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"PUT"
argument_list|)
expr_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
operator|new
name|EntityTag
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition must not be met"
argument_list|,
literal|412
argument_list|,
name|rb
operator|.
name|build
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeforeDate
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Modified-Since"
argument_list|,
literal|"Tue, 21 Oct 2008 14:00:00 GMT"
argument_list|)
expr_stmt|;
name|Date
name|serverDate
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss zzz"
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|parse
argument_list|(
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
name|serverDate
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Precondition must be met"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeforeDateIfNotModified
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_UNMODIFIED_SINCE
argument_list|,
literal|"Mon, 20 Oct 2008 14:00:00 GMT"
argument_list|)
expr_stmt|;
name|Date
name|serverDate
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss zzz"
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|parse
argument_list|(
literal|"Tue, 21 Oct 2008 14:00:00 GMT"
argument_list|)
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
name|serverDate
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition must not be met"
argument_list|,
literal|412
argument_list|,
name|rb
operator|.
name|build
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAfterDate
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Modified-Since"
argument_list|,
literal|"Tue, 21 Oct 2008 14:00:00 GMT"
argument_list|)
expr_stmt|;
name|Date
name|lastModified
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss zzz"
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|parse
argument_list|(
literal|"Mon, 20 Oct 2008 14:00:00 GMT"
argument_list|)
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
name|lastModified
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Precondition is not met"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|rb
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"If-Modified-Since precondition was not met"
argument_list|,
literal|304
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIfNoneMatchAndDateWithMatchingTags
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
literal|"\"123\""
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Modified-Since"
argument_list|,
literal|"Tue, 21 Oct 2008 14:00:00 GMT"
argument_list|)
expr_stmt|;
name|Date
name|lastModified
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss zzz"
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|parse
argument_list|(
literal|"Mon, 22 Oct 2008 14:00:00 GMT"
argument_list|)
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
name|lastModified
argument_list|,
operator|new
name|EntityTag
argument_list|(
literal|"\"123\""
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Precondition is not met"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIfNoneMatchAndDateWithNonMatchingTags
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|,
literal|"\"123\""
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|putSingle
argument_list|(
literal|"If-Modified-Since"
argument_list|,
literal|"Tue, 20 Oct 2008 14:00:00 GMT"
argument_list|)
expr_stmt|;
name|Date
name|lastModified
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss zzz"
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|parse
argument_list|(
literal|"Mon, 21 Oct 2008 14:00:00 GMT"
argument_list|)
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
operator|new
name|RequestImpl
argument_list|(
name|m
argument_list|)
operator|.
name|evaluatePreconditions
argument_list|(
name|lastModified
argument_list|,
operator|new
name|EntityTag
argument_list|(
literal|"124"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Dates must not be checked if tags do not match"
argument_list|,
name|rb
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

