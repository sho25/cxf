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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|servlet
package|;
end_package

begin_class
specifier|public
class|class
name|URIParser
block|{
specifier|private
name|String
name|servletPath
decl_stmt|;
specifier|private
name|String
name|requestUri
decl_stmt|;
specifier|private
name|String
name|pathInfo
decl_stmt|;
specifier|private
name|String
name|queryString
decl_stmt|;
specifier|public
name|URIParser
parameter_list|(
name|String
name|servletPath
parameter_list|)
block|{
name|this
operator|.
name|servletPath
operator|=
name|servletPath
expr_stmt|;
block|}
specifier|public
name|void
name|parse
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|int
name|indx
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|servletPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|this
operator|.
name|servletPath
operator|=
literal|"/"
operator|+
name|this
operator|.
name|servletPath
expr_stmt|;
block|}
if|if
condition|(
name|indx
operator|!=
operator|-
literal|1
condition|)
block|{
name|this
operator|.
name|pathInfo
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|servletPath
operator|.
name|length
argument_list|()
argument_list|,
name|indx
argument_list|)
expr_stmt|;
name|this
operator|.
name|queryString
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|indx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|requestUri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|indx
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|pathInfo
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|servletPath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|requestUri
operator|=
name|uri
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|pathInfo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|pathInfo
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|this
operator|.
name|pathInfo
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|this
operator|.
name|pathInfo
operator|=
literal|"/"
operator|+
name|this
operator|.
name|pathInfo
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
name|servletPath
return|;
block|}
specifier|public
name|String
name|getQueryString
parameter_list|()
block|{
return|return
name|queryString
return|;
block|}
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
name|this
operator|.
name|pathInfo
return|;
block|}
specifier|public
name|String
name|getRequestUri
parameter_list|()
block|{
return|return
name|requestUri
return|;
block|}
block|}
end_class

end_unit

