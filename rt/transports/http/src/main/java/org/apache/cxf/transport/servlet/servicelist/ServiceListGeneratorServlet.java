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
operator|.
name|servicelist
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|HashMap
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
name|HttpServlet
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
name|Bus
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
name|StringUtils
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
name|helpers
operator|.
name|IOUtils
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|AbstractDestination
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
name|DestinationRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|ServiceListGeneratorServlet
extends|extends
name|HttpServlet
block|{
specifier|private
name|DestinationRegistry
name|destinationRegistry
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|String
name|serviceListStyleSheet
decl_stmt|;
specifier|private
name|String
name|title
decl_stmt|;
specifier|private
name|boolean
name|showForeignContexts
init|=
literal|true
decl_stmt|;
specifier|public
name|ServiceListGeneratorServlet
parameter_list|(
name|DestinationRegistry
name|destinationRegistry
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|destinationRegistry
operator|=
name|destinationRegistry
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|this
operator|.
name|title
operator|=
literal|"CXF - Service list"
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
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|service
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|PrintWriter
name|writer
init|=
name|response
operator|.
name|getWriter
argument_list|()
decl_stmt|;
name|AbstractDestination
index|[]
name|destinations
init|=
name|destinationRegistry
operator|.
name|getSortedDestinations
argument_list|()
decl_stmt|;
if|if
condition|(
name|request
operator|.
name|getParameter
argument_list|(
literal|"stylesheet"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|renderStyleSheet
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|privateEndpoints
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atomMap
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|privateEndpoints
operator|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.private.endpoints"
argument_list|)
expr_stmt|;
comment|// TODO : we may introduce a bus extension instead
name|atomMap
operator|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.extensions.logging.atom.pull"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|privateEndpoints
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|atomMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|AbstractDestination
index|[]
name|soapEndpoints
init|=
name|getSOAPEndpoints
argument_list|(
name|destinations
argument_list|,
name|privateEndpoints
argument_list|)
decl_stmt|;
name|AbstractDestination
index|[]
name|restEndpoints
init|=
name|getRestEndpoints
argument_list|(
name|destinations
argument_list|,
name|privateEndpoints
argument_list|)
decl_stmt|;
name|ServiceListWriter
name|serviceListWriter
decl_stmt|;
if|if
condition|(
literal|"false"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getParameter
argument_list|(
literal|"formatted"
argument_list|)
argument_list|)
condition|)
block|{
name|boolean
name|renderWsdlList
init|=
literal|"true"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getParameter
argument_list|(
literal|"wsdlList"
argument_list|)
argument_list|)
decl_stmt|;
name|serviceListWriter
operator|=
operator|new
name|UnformattedServiceListWriter
argument_list|(
name|renderWsdlList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|styleSheetPath
decl_stmt|;
if|if
condition|(
name|serviceListStyleSheet
operator|!=
literal|null
condition|)
block|{
name|styleSheetPath
operator|=
name|request
operator|.
name|getContextPath
argument_list|()
operator|+
literal|"/"
operator|+
name|serviceListStyleSheet
expr_stmt|;
block|}
else|else
block|{
name|styleSheetPath
operator|=
name|request
operator|.
name|getRequestURI
argument_list|()
operator|+
literal|"/?stylesheet=1"
expr_stmt|;
block|}
name|serviceListWriter
operator|=
operator|new
name|FormattedServiceListWriter
argument_list|(
name|styleSheetPath
argument_list|,
name|title
argument_list|,
name|showForeignContexts
argument_list|,
name|atomMap
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setContentType
argument_list|(
name|serviceListWriter
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|basePath
init|=
name|request
operator|.
name|getAttribute
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|)
decl_stmt|;
name|serviceListWriter
operator|.
name|writeServiceList
argument_list|(
name|writer
argument_list|,
name|basePath
operator|==
literal|null
condition|?
literal|null
else|:
name|basePath
operator|.
name|toString
argument_list|()
argument_list|,
name|soapEndpoints
argument_list|,
name|restEndpoints
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isPrivate
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|privateEndpoints
parameter_list|)
block|{
if|if
condition|(
name|privateEndpoints
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|privateEndpoints
control|)
block|{
if|if
condition|(
name|ei
operator|.
name|getAddress
argument_list|()
operator|.
name|endsWith
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|AbstractDestination
index|[]
name|getSOAPEndpoints
parameter_list|(
name|AbstractDestination
index|[]
name|destinations
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|privateEndpoints
parameter_list|)
block|{
name|List
argument_list|<
name|AbstractDestination
argument_list|>
name|soapEndpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractDestination
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractDestination
name|sd
range|:
name|destinations
control|)
block|{
if|if
condition|(
literal|null
operator|!=
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|&&
literal|null
operator|!=
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|&&
operator|!
name|isPrivate
argument_list|(
name|sd
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
name|privateEndpoints
argument_list|)
condition|)
block|{
name|soapEndpoints
operator|.
name|add
argument_list|(
name|sd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|soapEndpoints
operator|.
name|toArray
argument_list|(
operator|new
name|AbstractDestination
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|private
name|AbstractDestination
index|[]
name|getRestEndpoints
parameter_list|(
name|AbstractDestination
index|[]
name|destinations
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|privateEndpoints
parameter_list|)
block|{
name|List
argument_list|<
name|AbstractDestination
argument_list|>
name|restfulDests
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractDestination
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractDestination
name|sd
range|:
name|destinations
control|)
block|{
comment|// use some more reasonable check - though this one seems to be the only option at the moment
if|if
condition|(
literal|null
operator|==
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|&&
operator|!
name|isPrivate
argument_list|(
name|sd
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
name|privateEndpoints
argument_list|)
condition|)
block|{
name|restfulDests
operator|.
name|add
argument_list|(
name|sd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|restfulDests
operator|.
name|toArray
argument_list|(
operator|new
name|AbstractDestination
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|private
name|void
name|renderStyleSheet
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/css; charset=UTF-8"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"servicelist.css"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|,
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
block|{
name|String
name|configServiceListStyleSheet
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
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|configServiceListStyleSheet
argument_list|)
condition|)
block|{
name|this
operator|.
name|serviceListStyleSheet
operator|=
name|configServiceListStyleSheet
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
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|configTitle
argument_list|)
condition|)
block|{
name|this
operator|.
name|title
operator|=
name|configTitle
expr_stmt|;
block|}
name|String
name|showAllContexts
init|=
name|servletConfig
operator|.
name|getInitParameter
argument_list|(
literal|"service-list-all-contexts"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|showAllContexts
argument_list|)
condition|)
block|{
name|this
operator|.
name|showForeignContexts
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|showAllContexts
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ServletConfig
name|getServletConfig
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getServletInfo
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{      }
block|}
end_class

end_unit

