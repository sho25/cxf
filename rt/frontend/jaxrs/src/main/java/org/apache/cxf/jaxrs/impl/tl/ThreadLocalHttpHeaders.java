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
operator|.
name|tl
package|;
end_package

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
name|core
operator|.
name|Cookie
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

begin_class
specifier|public
class|class
name|ThreadLocalHttpHeaders
extends|extends
name|AbstractThreadLocalProxy
argument_list|<
name|HttpHeaders
argument_list|>
implements|implements
name|HttpHeaders
block|{
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getAcceptableMediaTypes
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getAcceptableMediaTypes
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|getCookies
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getCookies
argument_list|()
return|;
block|}
specifier|public
name|Locale
name|getLanguage
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getLanguage
argument_list|()
return|;
block|}
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getMediaType
argument_list|()
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRequestHeaders
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getRequestHeaders
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Locale
argument_list|>
name|getAcceptableLanguages
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getAcceptableLanguages
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRequestHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|get
argument_list|()
operator|.
name|getRequestHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getDate
argument_list|()
return|;
block|}
specifier|public
name|String
name|getHeaderString
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|get
argument_list|()
operator|.
name|getHeaderString
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getLength
argument_list|()
return|;
block|}
block|}
end_class

end_unit

