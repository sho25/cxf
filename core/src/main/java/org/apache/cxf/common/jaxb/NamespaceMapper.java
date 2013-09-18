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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|xml
operator|.
name|bind
operator|.
name|marshaller
operator|.
name|NamespacePrefixMapper
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|NamespaceMapper
extends|extends
name|NamespacePrefixMapper
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EMPTY_STRING
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nspref
decl_stmt|;
specifier|private
name|String
index|[]
name|nsctxt
init|=
name|EMPTY_STRING
decl_stmt|;
specifier|public
name|NamespaceMapper
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nspref
parameter_list|)
block|{
name|this
operator|.
name|nspref
operator|=
name|nspref
expr_stmt|;
block|}
specifier|public
name|String
name|getPreferredPrefix
parameter_list|(
name|String
name|namespaceUri
parameter_list|,
name|String
name|suggestion
parameter_list|,
name|boolean
name|requirePrefix
parameter_list|)
block|{
name|String
name|prefix
init|=
name|nspref
operator|.
name|get
argument_list|(
name|namespaceUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
return|return
name|prefix
return|;
block|}
return|return
name|suggestion
return|;
block|}
specifier|public
name|void
name|setContextualNamespace
parameter_list|(
name|String
index|[]
name|contextualNamespaceDecls
parameter_list|)
block|{
name|this
operator|.
name|nsctxt
operator|=
name|contextualNamespaceDecls
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getContextualNamespaceDecls
parameter_list|()
block|{
return|return
name|nsctxt
return|;
block|}
block|}
end_class

end_unit

