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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|PathSegment
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
name|impl
operator|.
name|PathSegmentImpl
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|HttpUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|LOCAL_IP_ADDRESS
init|=
literal|"127.0.0.1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOCAL_HOST
init|=
literal|"localhost"
decl_stmt|;
specifier|private
name|HttpUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|URI
name|toAbsoluteUri
parameter_list|(
name|URI
name|u
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
operator|!
name|u
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|HttpServletRequest
name|httpRequest
init|=
operator|(
name|HttpServletRequest
operator|)
name|message
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpRequest
operator|!=
literal|null
condition|)
block|{
name|String
name|scheme
init|=
name|httpRequest
operator|.
name|isSecure
argument_list|()
condition|?
literal|"https"
else|:
literal|"http"
decl_stmt|;
name|String
name|host
init|=
name|httpRequest
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOCAL_IP_ADDRESS
operator|.
name|equals
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|host
operator|=
name|LOCAL_HOST
expr_stmt|;
block|}
name|int
name|port
init|=
name|httpRequest
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
return|return
name|URI
operator|.
name|create
argument_list|(
name|scheme
operator|+
literal|"://"
operator|+
name|host
operator|+
literal|':'
operator|+
name|port
operator|+
name|u
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|u
return|;
block|}
specifier|public
specifier|static
name|String
name|fromPathSegment
parameter_list|(
name|PathSegment
name|ps
parameter_list|)
block|{
if|if
condition|(
name|PathSegmentImpl
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|ps
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
operator|(
name|PathSegmentImpl
operator|)
name|ps
operator|)
operator|.
name|getOriginalPath
argument_list|()
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ps
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|ps
operator|.
name|getMatrixParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

