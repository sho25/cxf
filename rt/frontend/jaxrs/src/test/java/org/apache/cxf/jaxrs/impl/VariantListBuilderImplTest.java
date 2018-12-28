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
name|Variant
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|VariantListBuilderImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBuildAll
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|MediaType
name|mt1
init|=
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
decl_stmt|;
name|MediaType
name|mt2
init|=
operator|new
name|MediaType
argument_list|(
literal|"text"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|mediaTypes
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
operator|.
name|languages
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|)
operator|.
name|encodings
argument_list|(
literal|"zip"
argument_list|,
literal|"identity"
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"8 variants need to be created"
argument_list|,
literal|8
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildAllWithoutAdd
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|MediaType
name|mt1
init|=
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
decl_stmt|;
name|MediaType
name|mt2
init|=
operator|new
name|MediaType
argument_list|(
literal|"text"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|mediaTypes
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
operator|.
name|languages
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|)
operator|.
name|encodings
argument_list|(
literal|"zip"
argument_list|,
literal|"identity"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"8 variants need to be created"
argument_list|,
literal|8
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildTypeAndEnc
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|MediaType
name|mt1
init|=
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
decl_stmt|;
name|MediaType
name|mt2
init|=
operator|new
name|MediaType
argument_list|(
literal|"text"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|mediaTypes
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
operator|.
name|encodings
argument_list|(
literal|"zip"
argument_list|,
literal|"identity"
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"4 variants need to be created"
argument_list|,
literal|4
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildTypeAndLang
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|MediaType
name|mt1
init|=
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
decl_stmt|;
name|MediaType
name|mt2
init|=
operator|new
name|MediaType
argument_list|(
literal|"text"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|mediaTypes
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
operator|.
name|languages
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"8 variants need to be created"
argument_list|,
literal|4
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt1
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
name|mt2
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildLangAndEnc
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|languages
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|)
operator|.
name|encodings
argument_list|(
literal|"zip"
argument_list|,
literal|"identity"
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"4 variants need to be created"
argument_list|,
literal|4
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildLang
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|languages
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"fr"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2 variants need to be created"
argument_list|,
literal|2
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildEnc
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|encodings
argument_list|(
literal|"zip"
argument_list|,
literal|"identity"
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2 variants need to be created"
argument_list|,
literal|2
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|"identity"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildType
parameter_list|()
block|{
name|VariantListBuilderImpl
name|vb
init|=
operator|new
name|VariantListBuilderImpl
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
name|vb
operator|.
name|mediaTypes
argument_list|(
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
argument_list|,
operator|new
name|MediaType
argument_list|(
literal|"text"
argument_list|,
literal|"xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2 variants need to be created"
argument_list|,
literal|2
argument_list|,
name|variants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
operator|new
name|MediaType
argument_list|(
literal|"*"
argument_list|,
literal|"*"
argument_list|)
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|verifyVariant
argument_list|(
name|variants
argument_list|,
operator|new
name|Variant
argument_list|(
operator|new
name|MediaType
argument_list|(
literal|"text"
argument_list|,
literal|"xml"
argument_list|)
argument_list|,
operator|(
name|Locale
operator|)
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|verifyVariant
parameter_list|(
name|List
argument_list|<
name|Variant
argument_list|>
name|vs
parameter_list|,
name|Variant
name|var
parameter_list|)
block|{
for|for
control|(
name|Variant
name|v
range|:
name|vs
control|)
block|{
if|if
condition|(
name|v
operator|.
name|getLanguage
argument_list|()
operator|==
literal|null
operator|&&
name|v
operator|.
name|getEncoding
argument_list|()
operator|==
literal|null
operator|&&
name|v
operator|.
name|getMediaType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|encodCheck
init|=
name|v
operator|.
name|getEncoding
argument_list|()
operator|==
literal|null
operator|&&
name|var
operator|.
name|getEncoding
argument_list|()
operator|==
literal|null
operator|||
name|v
operator|.
name|getEncoding
argument_list|()
operator|.
name|equals
argument_list|(
name|var
operator|.
name|getEncoding
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|langCheck
init|=
name|v
operator|.
name|getLanguage
argument_list|()
operator|==
literal|null
operator|&&
name|var
operator|.
name|getLanguage
argument_list|()
operator|==
literal|null
operator|||
name|v
operator|.
name|getLanguage
argument_list|()
operator|.
name|equals
argument_list|(
name|var
operator|.
name|getLanguage
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|typeCheck
init|=
name|v
operator|.
name|getMediaType
argument_list|()
operator|==
literal|null
operator|&&
name|var
operator|.
name|getMediaType
argument_list|()
operator|==
literal|null
operator|||
name|v
operator|.
name|getMediaType
argument_list|()
operator|.
name|equals
argument_list|(
name|var
operator|.
name|getMediaType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodCheck
operator|&&
name|langCheck
operator|&&
name|typeCheck
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

