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
name|aegis
operator|.
name|type
operator|.
name|basic
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
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
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|DatabindingException
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
name|aegis
operator|.
name|type
operator|.
name|Type
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
name|aegis
operator|.
name|util
operator|.
name|date
operator|.
name|XsDateTimeFormat
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
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
import|;
end_import

begin_comment
comment|/**  * Type for the Calendar class.  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  */
end_comment

begin_class
specifier|public
class|class
name|CalendarType
extends|extends
name|Type
block|{
specifier|private
specifier|static
name|XsDateTimeFormat
name|format
init|=
operator|new
name|XsDateTimeFormat
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|String
name|value
init|=
name|reader
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|format
operator|.
name|parseObject
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not parse xs:dateTime: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|writer
operator|.
name|writeValue
argument_list|(
name|format
operator|.
name|format
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

