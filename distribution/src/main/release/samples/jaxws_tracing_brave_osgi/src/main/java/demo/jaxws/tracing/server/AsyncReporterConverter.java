begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxws
operator|.
name|tracing
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|ReifiedType
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|AsyncReporter
import|;
end_import

begin_comment
comment|/**  * Converts generic AsyncReporter<?> to AsyncReporter<Span> (see please  * https://issues.apache.org/jira/browse/ARIES-1607, https://issues.apache.org/jira/browse/ARIES-960  * and https://issues.apache.org/jira/browse/ARIES-1500)  *    */
end_comment

begin_class
specifier|public
class|class
name|AsyncReporterConverter
implements|implements
name|Converter
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Object
name|source
parameter_list|,
name|ReifiedType
name|target
parameter_list|)
block|{
return|return
operator|(
name|source
operator|instanceof
name|AsyncReporter
argument_list|<
name|?
argument_list|>
operator|&&
name|target
operator|.
name|getRawClass
argument_list|()
operator|==
name|AsyncReporter
operator|.
name|class
operator|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|Object
name|convert
parameter_list|(
name|Object
name|source
parameter_list|,
name|ReifiedType
name|target
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|source
operator|instanceof
name|AsyncReporter
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
operator|(
name|AsyncReporter
argument_list|<
name|Span
argument_list|>
operator|)
name|source
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to convert from "
operator|+
name|source
operator|+
literal|" to "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

