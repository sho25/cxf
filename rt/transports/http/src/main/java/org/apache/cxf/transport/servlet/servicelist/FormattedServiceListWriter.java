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
name|util
operator|.
name|Map
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
name|OperationInfo
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
name|servlet
operator|.
name|ServletDestination
import|;
end_import

begin_class
specifier|public
class|class
name|FormattedServiceListWriter
implements|implements
name|ServiceListWriter
block|{
specifier|private
name|String
name|styleSheetPath
decl_stmt|;
specifier|private
name|String
name|title
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atomMap
decl_stmt|;
specifier|private
name|boolean
name|showForeignContexts
decl_stmt|;
specifier|public
name|FormattedServiceListWriter
parameter_list|(
name|String
name|styleSheetPath
parameter_list|,
name|String
name|title
parameter_list|,
name|boolean
name|showForeignContexts
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atomMap
parameter_list|)
block|{
name|this
operator|.
name|styleSheetPath
operator|=
name|styleSheetPath
expr_stmt|;
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
name|this
operator|.
name|showForeignContexts
operator|=
name|showForeignContexts
expr_stmt|;
name|this
operator|.
name|atomMap
operator|=
name|atomMap
expr_stmt|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|"text/html; charset=UTF-8"
return|;
block|}
specifier|public
name|void
name|writeServiceList
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|basePath
parameter_list|,
name|AbstractDestination
index|[]
name|soapDestinations
parameter_list|,
name|AbstractDestination
index|[]
name|restDestinations
parameter_list|)
throws|throws
name|IOException
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
operator|+
literal|"\"http://www.w3.org/TR/html4/loose.dtd\">"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<HTML><HEAD>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<LINK type=\"text/css\" rel=\"stylesheet\" href=\""
operator|+
name|styleSheetPath
operator|+
literal|"\">"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<meta http-equiv=content-type content=\"text/html; charset=UTF-8\">"
argument_list|)
expr_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<title>"
operator|+
name|title
operator|+
literal|"</title>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<title>CXF - Service list</title>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"</head><body>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|soapDestinations
operator|.
name|length
operator|>
literal|0
operator|||
name|restDestinations
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|writeSOAPEndpoints
argument_list|(
name|writer
argument_list|,
name|basePath
argument_list|,
name|soapDestinations
argument_list|)
expr_stmt|;
name|writeRESTfulEndpoints
argument_list|(
name|writer
argument_list|,
name|basePath
argument_list|,
name|restDestinations
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<span class=\"heading\">No services have been found.</span>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"</body></html>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeSOAPEndpoints
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|basePath
parameter_list|,
name|AbstractDestination
index|[]
name|destinations
parameter_list|)
throws|throws
name|IOException
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<span class=\"heading\">Available SOAP services:</span><br/>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<table "
operator|+
operator|(
name|styleSheetPath
operator|.
name|endsWith
argument_list|(
literal|"stylesheet=1"
argument_list|)
condition|?
literal|"cellpadding=\"1\" cellspacing=\"1\" border=\"1\" width=\"100%\""
else|:
literal|""
operator|)
operator|+
literal|">"
argument_list|)
expr_stmt|;
for|for
control|(
name|AbstractDestination
name|sd
range|:
name|destinations
control|)
block|{
name|writerSoapEndpoint
argument_list|(
name|writer
argument_list|,
name|basePath
argument_list|,
name|sd
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"</table><br/><br/>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writerSoapEndpoint
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|basePath
parameter_list|,
name|AbstractDestination
name|sd
parameter_list|)
block|{
name|String
name|absoluteURL
init|=
name|getAbsoluteAddress
argument_list|(
name|basePath
argument_list|,
name|sd
argument_list|)
decl_stmt|;
if|if
condition|(
name|absoluteURL
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"<tr><td>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<span class=\"porttypename\">"
operator|+
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<ul>"
argument_list|)
expr_stmt|;
for|for
control|(
name|OperationInfo
name|oi
range|:
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|oi
operator|.
name|getProperty
argument_list|(
literal|"operation.is.synthetic"
argument_list|)
operator|!=
name|Boolean
operator|.
name|TRUE
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<li>"
operator|+
name|oi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"</li>"
argument_list|)
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"</ul>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"</td><td>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<span class=\"field\">Endpoint address:</span> "
operator|+
literal|"<span class=\"value\">"
operator|+
name|absoluteURL
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">WSDL :</span> "
operator|+
literal|"<a href=\""
operator|+
name|absoluteURL
operator|+
literal|"?wsdl\">"
operator|+
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"</a>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">Target namespace:</span> "
operator|+
literal|"<span class=\"value\">"
operator|+
name|sd
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|addAtomLinkIfNeeded
argument_list|(
name|absoluteURL
argument_list|,
name|atomMap
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"</td></tr>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getAbsoluteAddress
parameter_list|(
name|String
name|basePath
parameter_list|,
name|AbstractDestination
name|d
parameter_list|)
block|{
name|String
name|endpointAddress
init|=
operator|(
name|String
operator|)
name|d
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"publishedEndpointUrl"
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointAddress
operator|!=
literal|null
condition|)
block|{
return|return
name|endpointAddress
return|;
block|}
name|endpointAddress
operator|=
name|d
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpointAddress
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|||
name|endpointAddress
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
condition|)
block|{
if|if
condition|(
name|endpointAddress
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
return|return
name|endpointAddress
return|;
block|}
elseif|else
if|if
condition|(
name|showForeignContexts
condition|)
block|{
if|if
condition|(
name|d
operator|instanceof
name|ServletDestination
condition|)
block|{
name|String
name|path
init|=
operator|(
operator|(
name|ServletDestination
operator|)
name|d
operator|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
return|return
name|basePath
operator|+
name|path
return|;
block|}
else|else
block|{
return|return
name|endpointAddress
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
name|basePath
operator|+
name|endpointAddress
return|;
block|}
block|}
specifier|private
name|void
name|writeRESTfulEndpoints
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|basePath
parameter_list|,
name|AbstractDestination
index|[]
name|restfulDests
parameter_list|)
throws|throws
name|IOException
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"<span class=\"heading\">Available RESTful services:</span><br/>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<table "
operator|+
operator|(
name|styleSheetPath
operator|.
name|endsWith
argument_list|(
literal|"stylesheet=1"
argument_list|)
condition|?
literal|"cellpadding=\"1\" cellspacing=\"1\" border=\"1\" width=\"100%\""
else|:
literal|""
operator|)
operator|+
literal|">"
argument_list|)
expr_stmt|;
for|for
control|(
name|AbstractDestination
name|sd
range|:
name|restfulDests
control|)
block|{
name|writeRESTfulEndpoint
argument_list|(
name|writer
argument_list|,
name|basePath
argument_list|,
name|sd
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"</table>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeRESTfulEndpoint
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|basePath
parameter_list|,
name|AbstractDestination
name|sd
parameter_list|)
block|{
name|String
name|absoluteURL
init|=
name|getAbsoluteAddress
argument_list|(
name|basePath
argument_list|,
name|sd
argument_list|)
decl_stmt|;
if|if
condition|(
name|absoluteURL
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"<tr><td>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<span class=\"field\">Endpoint address:</span> "
operator|+
literal|"<span class=\"value\">"
operator|+
name|absoluteURL
operator|+
literal|"</span>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">WADL :</span> "
operator|+
literal|"<a href=\""
operator|+
name|absoluteURL
operator|+
literal|"?_wadl&_type=xml\">"
operator|+
name|absoluteURL
operator|+
literal|"?_wadl&type=xml"
operator|+
literal|"</a>"
argument_list|)
expr_stmt|;
name|addAtomLinkIfNeeded
argument_list|(
name|absoluteURL
argument_list|,
name|atomMap
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"</td></tr>"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addAtomLinkIfNeeded
parameter_list|(
name|String
name|address
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extMap
parameter_list|,
name|PrintWriter
name|pw
parameter_list|)
block|{
name|String
name|atomAddress
init|=
name|getExtensionEndpointAddress
argument_list|(
name|address
argument_list|,
name|extMap
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomAddress
operator|!=
literal|null
condition|)
block|{
name|pw
operator|.
name|write
argument_list|(
literal|"<br/><span class=\"field\">Atom Log Feed :</span> "
operator|+
literal|"<a href=\""
operator|+
name|atomAddress
operator|+
literal|"\">"
operator|+
name|atomAddress
operator|+
literal|"</a>"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getExtensionEndpointAddress
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extMap
parameter_list|)
block|{
if|if
condition|(
name|extMap
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|extMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|endpointAddress
operator|.
name|endsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|endpointAddress
operator|=
name|endpointAddress
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endpointAddress
operator|.
name|length
argument_list|()
operator|-
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|endpointAddress
operator|+=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
return|return
name|endpointAddress
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

