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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
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
name|Consumes
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
name|WebApplicationException
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
name|ext
operator|.
name|MessageBodyReader
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
name|ext
operator|.
name|Provider
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
name|jaxrs
operator|.
name|ext
operator|.
name|xml
operator|.
name|XMLSource
import|;
end_import

begin_class
annotation|@
name|Provider
annotation|@
name|Consumes
argument_list|(
block|{
literal|"text/xml"
block|,
literal|"application/xml"
block|,
literal|"application/*+xml"
block|}
argument_list|)
specifier|public
class|class
name|XPathProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|consumeMediaTypes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|classExpressions
decl_stmt|;
specifier|private
name|String
name|globalExpression
decl_stmt|;
specifier|private
name|String
name|className
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|globalNamespaces
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setConsumeMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|consumeMediaTypes
operator|=
name|types
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConsumeMediaTypes
parameter_list|()
block|{
return|return
name|consumeMediaTypes
return|;
block|}
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|expr
parameter_list|)
block|{
name|globalExpression
operator|=
name|expr
expr_stmt|;
block|}
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|className
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setExpressions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expressions
parameter_list|)
block|{
name|classExpressions
operator|=
name|expressions
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|)
block|{
name|globalNamespaces
operator|=
name|nsMap
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mediaType
parameter_list|)
block|{
return|return
name|globalExpression
operator|!=
literal|null
operator|&&
operator|(
name|className
operator|==
literal|null
operator|||
name|className
operator|.
name|equals
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|||
name|classExpressions
operator|!=
literal|null
operator|&&
name|classExpressions
operator|.
name|containsKey
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|T
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hrs
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|String
name|expression
init|=
name|globalExpression
operator|!=
literal|null
condition|?
name|globalExpression
else|:
name|classExpressions
operator|.
name|get
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
comment|// must not happen if isReadable() returned true
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|500
argument_list|)
throw|;
block|}
name|XMLSource
name|source
init|=
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
return|return
name|source
operator|.
name|getNode
argument_list|(
name|expression
argument_list|,
name|globalNamespaces
argument_list|,
name|cls
argument_list|)
return|;
block|}
block|}
end_class

end_unit

