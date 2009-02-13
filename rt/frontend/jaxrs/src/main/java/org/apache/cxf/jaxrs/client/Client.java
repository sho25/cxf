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
name|client
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
name|Date
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
name|EntityTag
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
name|core
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  * Represents common proxy and http-centric client capabilities  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|Client
block|{
comment|/**      * sets HTTP Content-Type header      * @param ct JAXRS MediaType representing Content-Type value        * @return the updated Client      */
name|Client
name|type
parameter_list|(
name|MediaType
name|ct
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Content-Type header      * @param type Content-Type value        * @return the updated Client      */
name|Client
name|type
parameter_list|(
name|String
name|type
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Accept header      * @param types list of JAXRS MediaTypes representing Accept header values        * @return the updated Client      */
name|Client
name|accept
parameter_list|(
name|MediaType
modifier|...
name|types
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Accept header      * @param types list of Accept header values        * @return the updated Client      */
name|Client
name|accept
parameter_list|(
name|String
modifier|...
name|types
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Content-Language header       * @param language Content-Language header value        * @return the updated Client      */
name|Client
name|language
parameter_list|(
name|String
name|language
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Accept-Language header       * @param languages list of Accept-Language header values        * @return the updated Client      */
name|Client
name|acceptLanguage
parameter_list|(
name|String
modifier|...
name|languages
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Content-Encoding header       * @param encoding Content-Encoding header value        * @return the updated Client      */
name|Client
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Accept-Encoding header       * @param encodings list of Accept-Encoding header value        * @return the updated Client      */
name|Client
name|acceptEncoding
parameter_list|(
name|String
modifier|...
name|encodings
parameter_list|)
function_decl|;
comment|/**      * sets HTTP If-Match or If-None-Match header      * @param tag ETag value      * @param ifNot if true then If-None-Match is set, If-Match otherwise        * @return the updated Client      */
name|Client
name|match
parameter_list|(
name|EntityTag
name|tag
parameter_list|,
name|boolean
name|ifNot
parameter_list|)
function_decl|;
comment|/**      * sets HTTP If-Modified-Since or If-Unmodified-Since header      * @param date Date value, will be formated as "EEE, dd MMM yyyy HH:mm:ss zzz"       * @param ifNot if true then If-Unmodified-Since is set, If-Modified-Since otherwise        * @return the updated Client      */
name|Client
name|modified
parameter_list|(
name|Date
name|date
parameter_list|,
name|boolean
name|ifNot
parameter_list|)
function_decl|;
comment|/**      * sets HTTP Cookie header       * @param cookie Cookie value        * @return the updated Client      */
name|Client
name|cookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
function_decl|;
comment|/**      * Sets arbitrary HTTP Header      * @param name header name      * @param values list of header values      * @return the updated Client      */
name|Client
name|header
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
function_decl|;
comment|/**      * Sets HTTP Headers      * @param map headers      * @return the updated Client      */
name|Client
name|headers
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
function_decl|;
comment|/**      * Resets the headers and response state if any      * @return  the updated Client      */
name|Client
name|reset
parameter_list|()
function_decl|;
comment|/**      * Gets the copy of request headers      * @return request headers      */
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
function_decl|;
comment|/**      * Gets the base URI this Client has been intialized with      * @return base URI      */
name|URI
name|getBaseURI
parameter_list|()
function_decl|;
comment|/**      * Gets the current URI this Client is working with      * @return current URI      */
name|URI
name|getCurrentURI
parameter_list|()
function_decl|;
comment|/**      * Gets the response state if any      * @return JAXRS Response response      * @throws IllegalStateException if no request has been made or this method called more than once       */
name|Response
name|getResponse
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

