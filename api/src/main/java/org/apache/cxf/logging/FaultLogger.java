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
name|logging
package|;
end_package

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

begin_comment
comment|/**  * Implement this interface to customize logging behavior for Exceptions  * thrown by the application implementing the service call.  * Implementations of this class must be registered in configuration of CXF  * to be invoked.  *  * Implementing this interface can also be used for listening to exceptions  * that occur in the service, as long as they are not caught in the application.  */
end_comment

begin_interface
specifier|public
interface|interface
name|FaultLogger
block|{
comment|/**      * Handle logging for the occured exception.      * @param exception The exception      * @param description A description of where in the service interfaces      * the exception occurred.      * @param message the message processed while the exception occured.      * @return<code>true</code> if CXF should use default logging for this      * exception,<code>false</code> if CXF not should do any logging.      */
name|boolean
name|log
parameter_list|(
name|Exception
name|exception
parameter_list|,
name|String
name|description
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

