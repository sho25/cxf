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
name|servlet
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
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|common
operator|.
name|util
operator|.
name|UrlUtils
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
specifier|abstract
class|class
name|AbstractServletController
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|DEFAULT_LISTINGS_CLASSIFIER
init|=
literal|"/services"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ServletController
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|boolean
name|isHideServiceList
decl_stmt|;
specifier|protected
name|boolean
name|disableAddressUpdates
decl_stmt|;
specifier|protected
name|String
name|forcedBaseAddress
decl_stmt|;
specifier|protected
name|String
name|serviceListStyleSheet
decl_stmt|;
specifier|protected
name|String
name|title
decl_stmt|;
specifier|protected
name|String
name|serviceListRelativePath
init|=
name|DEFAULT_LISTINGS_CLASSIFIER
decl_stmt|;
specifier|protected
name|ServletConfig
name|servletConfig
decl_stmt|;
specifier|protected
name|AbstractServletController
parameter_list|()
block|{              }
specifier|protected
name|AbstractServletController
parameter_list|(
name|ServletConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|servletConfig
operator|=
name|config
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setHideServiceList
parameter_list|(
name|boolean
name|generate
parameter_list|)
block|{
name|isHideServiceList
operator|=
name|generate
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceListRelativePath
parameter_list|(
name|String
name|relativePath
parameter_list|)
block|{
name|serviceListRelativePath
operator|=
name|relativePath
expr_stmt|;
block|}
specifier|public
name|void
name|setDisableAddressUpdates
parameter_list|(
name|boolean
name|noupdates
parameter_list|)
block|{
name|disableAddressUpdates
operator|=
name|noupdates
expr_stmt|;
block|}
specifier|public
name|void
name|setForcedBaseAddress
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|forcedBaseAddress
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceListStyleSheet
parameter_list|(
name|String
name|serviceListStyleSheet
parameter_list|)
block|{
name|this
operator|.
name|serviceListStyleSheet
operator|=
name|serviceListStyleSheet
expr_stmt|;
block|}
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|title
operator|=
name|t
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|servletConfig
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|hideServiceList
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"hide-service-list-page"
argument_list|)
decl_stmt|;
if|if
condition|(
name|hideServiceList
operator|!=
literal|null
condition|)
block|{
name|isHideServiceList
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|hideServiceList
argument_list|)
expr_stmt|;
block|}
name|String
name|isDisableAddressUpdates
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"disable-address-updates"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDisableAddressUpdates
operator|!=
literal|null
condition|)
block|{
name|disableAddressUpdates
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|isDisableAddressUpdates
argument_list|)
expr_stmt|;
block|}
name|String
name|isForcedBaseAddress
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"base-address"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isForcedBaseAddress
operator|!=
literal|null
condition|)
block|{
name|forcedBaseAddress
operator|=
name|isForcedBaseAddress
expr_stmt|;
block|}
name|String
name|serviceListTransform
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-stylesheet"
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceListTransform
operator|!=
literal|null
condition|)
block|{
name|serviceListStyleSheet
operator|=
name|serviceListTransform
expr_stmt|;
block|}
name|String
name|serviceListPath
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-path"
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceListPath
operator|!=
literal|null
condition|)
block|{
name|serviceListRelativePath
operator|=
name|serviceListPath
expr_stmt|;
block|}
name|String
name|configTitle
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-title"
argument_list|)
decl_stmt|;
if|if
condition|(
name|configTitle
operator|!=
literal|null
condition|)
block|{
name|title
operator|=
name|configTitle
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getBaseURL
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|String
name|reqPrefix
init|=
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|pathInfo
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
comment|//fix for CXF-898
if|if
condition|(
operator|!
literal|"/"
operator|.
name|equals
argument_list|(
name|pathInfo
argument_list|)
operator|||
name|reqPrefix
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// needs to be done given that pathInfo is decoded
comment|// TODO : it's unlikely servlet path will contain encoded values so we're most
comment|// likely safe however we need to ensure if it happens then this code works properly too
name|reqPrefix
operator|=
name|UrlUtils
operator|.
name|pathDecode
argument_list|(
name|reqPrefix
argument_list|)
expr_stmt|;
comment|// pathInfo drops matrix parameters attached to a last path segment
name|int
name|offset
init|=
literal|0
decl_stmt|;
name|int
name|index
init|=
name|getMatrixParameterIndex
argument_list|(
name|reqPrefix
argument_list|,
name|pathInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
name|pathInfo
operator|.
name|length
argument_list|()
condition|)
block|{
name|offset
operator|=
name|reqPrefix
operator|.
name|length
argument_list|()
operator|-
name|index
expr_stmt|;
block|}
name|reqPrefix
operator|=
name|reqPrefix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|reqPrefix
operator|.
name|length
argument_list|()
operator|-
name|pathInfo
operator|.
name|length
argument_list|()
operator|-
name|offset
argument_list|)
expr_stmt|;
block|}
return|return
name|reqPrefix
return|;
block|}
specifier|private
name|int
name|getMatrixParameterIndex
parameter_list|(
name|String
name|reqPrefix
parameter_list|,
name|String
name|pathInfo
parameter_list|)
block|{
name|int
name|index
init|=
name|reqPrefix
operator|.
name|lastIndexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
name|int
name|lastIndex
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|index
operator|>=
name|pathInfo
operator|.
name|length
argument_list|()
condition|)
block|{
name|lastIndex
operator|=
name|index
expr_stmt|;
name|reqPrefix
operator|=
name|reqPrefix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
if|if
condition|(
name|reqPrefix
operator|.
name|endsWith
argument_list|(
name|pathInfo
argument_list|)
condition|)
block|{
break|break;
block|}
name|index
operator|=
name|reqPrefix
operator|.
name|lastIndexOf
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
return|return
name|lastIndex
return|;
block|}
specifier|public
name|void
name|invokeDestination
parameter_list|(
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|AbstractHTTPDestination
name|d
parameter_list|)
throws|throws
name|ServletException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Service http request on thread: "
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|d
operator|.
name|invoke
argument_list|(
name|servletConfig
argument_list|,
name|servletConfig
operator|.
name|getServletContext
argument_list|()
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished servicing http request on thread: "
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

