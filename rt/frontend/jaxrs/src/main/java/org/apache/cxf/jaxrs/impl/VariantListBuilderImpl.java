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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Variant
operator|.
name|VariantListBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|VariantListBuilderImpl
extends|extends
name|VariantListBuilder
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|encodings
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Locale
argument_list|>
name|languages
init|=
operator|new
name|ArrayList
argument_list|<
name|Locale
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MediaType
argument_list|>
name|mediaTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|MediaType
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
init|=
operator|new
name|ArrayList
argument_list|<
name|Variant
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|VariantListBuilderImpl
parameter_list|()
block|{              }
annotation|@
name|Override
specifier|public
name|VariantListBuilder
name|add
parameter_list|()
block|{
name|addVariants
argument_list|()
expr_stmt|;
name|resetMeta
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Variant
argument_list|>
name|build
parameter_list|()
block|{
name|List
argument_list|<
name|Variant
argument_list|>
name|vs
init|=
operator|new
name|ArrayList
argument_list|<
name|Variant
argument_list|>
argument_list|(
name|variants
argument_list|)
decl_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|vs
return|;
block|}
annotation|@
name|Override
specifier|public
name|VariantListBuilder
name|encodings
parameter_list|(
name|String
modifier|...
name|encs
parameter_list|)
block|{
name|encodings
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|encs
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|VariantListBuilder
name|mediaTypes
parameter_list|(
name|MediaType
modifier|...
name|types
parameter_list|)
block|{
name|mediaTypes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|types
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|void
name|reset
parameter_list|()
block|{
name|variants
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resetMeta
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|resetMeta
parameter_list|()
block|{
name|mediaTypes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|languages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|encodings
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|addVariants
parameter_list|()
block|{
if|if
condition|(
name|mediaTypes
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|handleMediaTypes
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|languages
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|handleLanguages
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|encodings
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|enc
range|:
name|encodings
control|)
block|{
name|variants
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|enc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|handleMediaTypes
parameter_list|()
block|{
for|for
control|(
name|MediaType
name|type
range|:
name|mediaTypes
control|)
block|{
if|if
condition|(
name|languages
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|handleLanguages
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|encodings
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|enc
range|:
name|encodings
control|)
block|{
name|variants
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|enc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|variants
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|handleLanguages
parameter_list|(
name|MediaType
name|type
parameter_list|)
block|{
for|for
control|(
name|Locale
name|lang
range|:
name|languages
control|)
block|{
if|if
condition|(
name|encodings
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|enc
range|:
name|encodings
control|)
block|{
name|variants
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|type
argument_list|,
name|lang
argument_list|,
name|enc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|variants
operator|.
name|add
argument_list|(
operator|new
name|Variant
argument_list|(
name|type
argument_list|,
name|lang
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|VariantListBuilder
name|languages
parameter_list|(
name|Locale
modifier|...
name|ls
parameter_list|)
block|{
name|languages
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ls
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

